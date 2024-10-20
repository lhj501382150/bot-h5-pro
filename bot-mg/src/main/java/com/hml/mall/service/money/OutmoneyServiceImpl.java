package com.hml.mall.service.money;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hml.backcore.utils.ZbUtils;
import com.hml.core.page.MybatisPlusPageHelper;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.heepay.service.IHeePayService;
import com.hml.mall.entity.money.Outmoney;
import com.hml.mall.iface.money.IOutmoneyService;
import com.hml.mall.mapper.money.OutmoneyMapper;
import com.hml.mall.security.LoginUser;
import com.hml.mall.util.SecurityUtils;
import com.hml.utils.DateTimeUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hml
 * @since 2021-11-19
 */

@Service("outmoneyService")
@Slf4j
public class OutmoneyServiceImpl extends ServiceImpl<OutmoneyMapper, Outmoney> implements IOutmoneyService {

	@Autowired
	private OutmoneyMapper outmoneyMapper;
	
	@Autowired
	private IHeePayService heePayService;

    @Override
    public List< Outmoney> list(Outmoney model) {
       QueryWrapper< Outmoney> wrapper = new QueryWrapper();
       return super.list(wrapper);
    }

    @Override
	public PageResult findPage(PageRequest pageRequest) {
    	LoginUser user = SecurityUtils.getLoginInfo();
    	if(user.getType() > 0) {
    		pageRequest.getParams().put("userno", user.getUserno());
    	}
    	PageResult result = MybatisPlusPageHelper.findPage(pageRequest, outmoneyMapper,"findPage");
    	if(result.getContent() !=null && result.getContent().size() > 0){
			QueryWrapper<?> qw = MybatisPlusPageHelper.getQueryWrapper(pageRequest);
			Outmoney sum = outmoneyMapper.findSum(qw);
			result.setSum(sum);
		}
		return result;
	}
    
    @Override
	public PageResult findExportPage(PageRequest pageRequest) {
    	LoginUser user = SecurityUtils.getLoginInfo();
    	if(user.getType() > 0) {
    		pageRequest.getParams().put("userno", user.getUserno());
    	}
    	PageResult result = MybatisPlusPageHelper.findPage(pageRequest, outmoneyMapper,"findExportPage");
    	 
		return result;
	}
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void checkOutMoney(Outmoney model) throws Exception {
    	Outmoney item = outmoneyMapper.selectById(model.getId());
    	if(!"0".equals(item.getChkstatus())) {
    		throw new Exception("该申请已处理！");
    	}
    	item.setChkstatus(model.getChkstatus());
    	item.setChkuserno(model.getChkuserno());
    	item.setChkip(model.getChkip());
    	item.setChktime(DateTimeUtils.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
    	outmoneyMapper.updateById(item);
    	
//    	驳回通过 调整资金
    	if("2".equals(item.getChkstatus())){
        	String userno = item.getUserno();
        	String actno = "100";
        	String subno = "102";//收入
        	String msubno = "1002";
        	String money = item.getChmoney().toString();
        	String contno = "TX_" + item.getId();
        	String memo = item.getMemo();
        	ZbUtils.changeMoney(userno, actno, subno, money, contno, memo,msubno);
    	}
     }
    /**
	 * 商户流水号^银行编号^对公对私^收款人帐号^收款人姓名^付款金额^付款理由^省份^城市^收款支行名称^联行号^身份证号”来组织数据
	 * ，联行号和身份证号可以为空。每条整数据间用“|”符号分隔
	 *  说明	编号 对私	0 对公	1
	 */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void payOutMoney(List<Long> ids) throws Exception {
    	 log.info("支付记录：" + ids);
    	 StringBuffer buff2 = new StringBuffer();
    	 for(Long id:ids) {
    		 buff2.append(id).append(",");
    	 }
    	 String str = buff2.toString();
    	 str = str.substring(0,str.length() -1 );
    	 
    	 List<Outmoney> list = outmoneyMapper.findPayMoney(str);
    	 StringBuffer buff = new StringBuffer();
    	 BigDecimal money = new BigDecimal(0);
    	 for(Outmoney item : list) {
     		if(!"1".equals(item.getChkstatus())) {
     			throw new Exception("存在数据未审核，暂不能支付");
     		}
     		if(!"100".equals(item.getPaystatus())) {
     			throw new Exception("存在数据已支付，请勿重复支付");
     		}
     		buff.append(formatWater(item)).append("^");
     		buff.append(item.getBANKNO()).append("^");
     		buff.append("0").append("^");
     		buff.append(item.getCARDNO()).append("^");
     		buff.append(item.getSNAME()).append("^");
     		buff.append(item.getOutmoney()).append("^");
     		buff.append("拍卖结算").append("^");
     		buff.append(item.getPROVINCE()).append("^");
     		buff.append(item.getCITY()).append("^");
     		buff.append(item.getSUBBANK()).append("^");
     		buff.append("").append("^");
     		buff.append(item.getIDNO()).append("|");
     		
     		money = money.add(item.getOutmoney());
    	 }
    	 String detail = buff.toString();
    	 detail = detail.substring(0,detail.length() - 1);
    	 
    	 Map<String,String> map = new HashedMap();
    	 map.put("batch_amt",money.toString());
    	 map.put("batch_num", String.valueOf(list.size()));
    	 map.put("detail_data", detail);
    	 
    	 heePayService.pay(map);
    	 
    	 log.info("【更新支付状态】：{}",ids);
    	 for(Long id :ids) {
    		 outmoneyMapper.updatePayStatus(id);
    	 }
    	 
    }
    
    public String formatWater(Outmoney order) {
		String fdate = order.getFdate();
		fdate = fdate.replaceAll("-", "");
		return fdate + String.format("%08d", order.getId());
	}
}

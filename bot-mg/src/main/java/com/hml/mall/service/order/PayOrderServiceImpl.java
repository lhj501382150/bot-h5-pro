package com.hml.mall.service.order;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hml.backcore.config.BackCoreConfig;
import com.hml.backcore.req.REQ530;
import com.hml.backcore.utils.ZbUtils;
import com.hml.core.page.MybatisPlusPageHelper;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.heepay.service.IHeePayService;
import com.hml.mall.entity.order.PayOrder;
import com.hml.mall.iface.order.IPayOrderService;
import com.hml.mall.mapper.order.PayOrderMapper;
import com.hml.mall.security.LoginUser;
import com.hml.mall.util.SecurityUtils;
import com.hml.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hml
 * @since 2021-07-10
 */

@Service("payOrderService")
@Slf4j
public class PayOrderServiceImpl extends ServiceImpl<PayOrderMapper, PayOrder> implements IPayOrderService {

	@Autowired
	private PayOrderMapper payOrderMapper;
	
	@Autowired
	private IHeePayService heePayService;
	
    @Override
    public List< PayOrder> list(PayOrder model) {
       QueryWrapper< PayOrder> wrapper = new QueryWrapper();
       return super.list(wrapper);
    }

    @Override
	public PageResult findPage(PageRequest pageRequest) {
    	LoginUser user = SecurityUtils.getLoginInfo();
		if(user==null) {
			return null;
		}
		if(user.getType() > 0) {
//			当前登陆用户
			int clevel = user.getClevel();
			Object uno1 = pageRequest.getParam("uno1");
			Object uno2 = pageRequest.getParam("uno2");
			pageRequest.getParams().remove("uno1");
			pageRequest.getParams().remove("uno2");
			if(!StringUtils.isBlank(uno1)) {
				int c1 = clevel + 1;
				pageRequest.getParams().put("uno"+c1, uno1);
			}
			if(!StringUtils.isBlank(uno2)) {
				int c1 = clevel + 2;
				pageRequest.getParams().put("uno"+c1, uno2);
			}
//			分销商/客户 查询 配置当前登陆信息
			pageRequest.getParams().put(user.getQueryNo(), SecurityUtils.getUsername());
		}
		PageResult pageResult =  MybatisPlusPageHelper.findPage(pageRequest, payOrderMapper,"findPageByUser");
		 if(pageResult.getContent().size() > 0) {//取合计
			 QueryWrapper qw = MybatisPlusPageHelper.getQueryWrapper(pageRequest);
			 PayOrder sum = payOrderMapper.selectSum(qw);
			 pageResult.setSum(sum);
		 }
		 
		 return pageResult;
	}
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOrder(PayOrder order) throws Exception {
    	PayOrder item = payOrderMapper.selectById(order.getWartno());
    	if(!"200".equals(item.getPaystatus())){
    		throw new Exception("订单未支付成功，不可同步！");
    	}
    	item.setSync("Y");
    	payOrderMapper.updateById(item);
//    	 发送账本
//		 发送账本
		 REQ530 req = new REQ530();
		 req.setWARTNO(String.valueOf(item.getWartno()));
		 req.setPAYSTATUS(item.getPaystatus());
		 req.setDETYPE("4");
		 req.setUSERNO(item.getUserno());
		 ZbUtils.post(BackCoreConfig.TRADE530, req);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject queryOrderStatus(PayOrder order) throws Exception {
    	order = payOrderMapper.selectById(order.getWartno());
//    	if("100".equals(order.getPaystatus())){
//    		throw new Exception("订单未支付");
//    	}
    	Map<String, String> ret = heePayService.queryStatus(order);
    	JSONObject json = (JSONObject)JSONObject.toJSON(ret);
    	return json;
    }
    
  //String.format("%08d", item.getWartno());
  	private Long getWater(String tradeNo) {
  		String sub = tradeNo.substring(8);
  		return Long.parseLong(sub);
  	}
  	
  	private String formatWater(PayOrder order) {
  		String fdate = order.getFdate();
  		fdate = fdate.replaceAll("-", "");
  		return fdate + String.format("%08d", order.getWartno());
  	}
}

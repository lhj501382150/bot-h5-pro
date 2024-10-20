package com.hml.mall.service.trade;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hml.core.page.MybatisPlusPageHelper;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.trade.Ware;
import com.hml.mall.entity.trade.WareApply;
import com.hml.mall.entity.trade.WareAttrApply;
import com.hml.mall.entity.trade.WareSc;
import com.hml.mall.iface.trade.IWareApplyService;
import com.hml.mall.mapper.trade.WareApplyMapper;
import com.hml.mall.mapper.trade.WareAttrApplyMapper;
import com.hml.mall.mapper.trade.WareMapper;
import com.hml.mall.mapper.trade.WareScMapper;
import com.hml.mall.security.LoginUser;
import com.hml.mall.util.SecurityUtils;
import com.hml.utils.DateTimeUtils;
import com.hml.utils.StringUtils;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hml
 * @since 2021-05-18
 */

@Service("wareApplyService")
public class WareApplyServiceImpl extends ServiceImpl<WareApplyMapper, WareApply> implements IWareApplyService {
	private Logger log = Logger.getLogger(WareApplyServiceImpl.class);
	@Autowired
	private WareApplyMapper wareApplyMapper;

	@Autowired
	private WareMapper wareMapper;
	
	@Autowired
	private WareScMapper wareScMapper;
	
	@Autowired
	private WareAttrApplyMapper wareAttrApplyMapper;
	
	@Override
	public WareApply getById(Serializable id) {
		WareApply ware = super.getById(id);
		if(ware == null) {
			return null;
		}
//		加载属性信息
		QueryWrapper<WareAttrApply> attr = new QueryWrapper<WareAttrApply>();
		attr.eq("WATERID", id);
		List<WareAttrApply> attrs = wareAttrApplyMapper.selectList(attr);
		if(attrs.size() <=0) {//商品未设置 加载大类属性
			attrs = wareAttrApplyMapper.selectWareAttr(ware);
		}
		ware.setWareAttrs(attrs);
		return ware;
	}

    @Override
    public List< WareApply> list(WareApply model) {
       QueryWrapper< WareApply> wrapper = new QueryWrapper();
       return super.list(wrapper);
    }

    @Override
	public PageResult findPage(PageRequest pageRequest) {
    	LoginUser user = SecurityUtils.getLoginInfo();
    	if(user.getType() > 0) {//非管理员
    		pageRequest.getParams().put("userno",user.getUserno());
    	}
		return MybatisPlusPageHelper.findPage(pageRequest, wareApplyMapper,"findPage");
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveWare(WareApply ware) throws Exception {
		log.info("会员录入商品信息:" + ware);
		LoginUser user = SecurityUtils.getLoginInfo();
		ware.setUserno(user.getLoginno());
		ware.setStatus(0);
		ware.setModule("A,");
		int id = wareApplyMapper.insert(ware);

		long waterid = ware.getWaterid();
//		保存商品属性信息
		if(ware.getWareAttrs() !=null) {
			List<WareAttrApply> attrs = ware.getWareAttrs();
			for(WareAttrApply attr:attrs) {
				attr.setWaterid(waterid);
				wareAttrApplyMapper.insert(attr);
			}
		}

		
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateWare(WareApply ware) throws Exception {
//		删除商品
		delWare(ware);
//		更新商品信息
		saveWare(ware);
		
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delWare(WareApply ware) throws Exception {
		long waterid = ware.getWaterid();
 
		QueryWrapper<WareAttrApply> attr = new QueryWrapper<WareAttrApply>();
		attr.eq("WATERID", waterid);
//		删除商品属性信息
		wareAttrApplyMapper.delete(attr);
//		删除商品信息
		wareApplyMapper.deleteById(waterid);
	}
	
	@Override
	public List<WareAttrApply> findWareAttr(WareApply ware) throws Exception {
		log.info("查询商品信息：" + ware.toString());
		if(ware.getWaterid() == null) {
			throw new Exception("商品编号不能为空");
		}
		QueryWrapper<WareAttrApply> qw = new QueryWrapper<WareAttrApply>();
		qw.eq("WATERID", ware.getWaterid());
		List<WareAttrApply> list = wareAttrApplyMapper.selectList(qw);
		return list;
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void submitWareApply(WareApply ware) throws Exception {
		if(ware.getWaterid() == null) {
			throw new Exception("参数不正确");
		}
		
		WareApply data = wareApplyMapper.selectById(ware.getWaterid());
		if(data.getStatus() ==1) {
			throw new Exception("该数据已提交，请等待管理员审核");
		}
		
		if(data.getStatus() ==2 ) {
			throw new Exception("该数据已审核");
		}
//		提交
		data.setStatus(1);
		
		wareApplyMapper.updateById(data);
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void auditWareApply(WareApply ware) throws Exception {
		log.info("审核录入商品：" + ware);
		if(ware.getWaterid() == null) {
			throw new Exception("参数不正确");
		}
		WareApply data = wareApplyMapper.selectById(ware.getWaterid());
		if(data.getStatus() != 1) {
			throw new Exception("请核对数据状态是否正确");
		}
//		审核
		if(ware.getStatus()==2) {//审核通过
			data.setStatus(2);
			data.setCheckdate(DateTimeUtils.getDateTime());
		}else {
			data.setStatus(3);
			data.setCheckdate(DateTimeUtils.getDateTime());
			data.setMemo(ware.getMemo());
		}
		if(StringUtils.isBlank(ware.getWareno())){
			String wareno = DateTimeUtils.getCurrentDate("yyyyMMdd") + data.getWaterid();
			data.setWareno(wareno);
		}else{
			data.setWareno(ware.getWareno());
		}
		
		wareApplyMapper.updateById(data);
		
//		 审核通过
		if(data.getStatus() !=2) return;
		Ware w = getWare(data);
		WareSc sc = getWareSc(data);
//		保存商品
		wareMapper.insert(w);
//		保存商城信息
		wareScMapper.insert(sc);
//		保存属性信息
		 wareAttrApplyMapper.updateWareAttr(data);
		 wareAttrApplyMapper.saveWareAttr(data);
	}

	
	 private Ware getWare(WareApply a) {
	    	Ware w = new Ware();
	    	w.setWareno(a.getWareno());
	    	w.setWarename(a.getWarename());
	    	w.setCno(a.getCno());
	    	w.setSeq(a.getSeq());
	    	w.setUnit(a.getUnit());
	    	w.setFilepath(a.getFilepath());
	    	w.setPicpath(a.getPicpath());
	    	w.setVideopath(a.getVideopath());
	    	w.setModule("A,");
	    	
	    	return w;
	    }
	    
	    private WareSc getWareSc(WareApply a) {
	    	WareSc sc = new WareSc();
	    	sc.setWareno(a.getWareno());
	    	sc.setCno(a.getCno());
	    	sc.setTitle(a.getTitle());
	    	sc.setFlag(a.getFlag());
	    	sc.setIntenum(a.getIntenum());
	    	sc.setInteprice(a.getInteprice());
	    	sc.setNum(a.getNum());
	    	sc.setPrice(a.getPrice());
	    	sc.setSalnum(a.getSalnum());
	    	sc.setSalprice(a.getSalprice());
	    	sc.setSpec(a.getSpec());
	    	sc.setTranscomm(a.getTranscomm());
	    	sc.setTranscomp(a.getTranscomp());
	    	sc.setWeight(a.getWeight());
	    	return sc;
	    }
}



package com.hml.mall.service.trade;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hml.core.page.MybatisPlusPageHelper;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.trade.Basepara;
import com.hml.mall.entity.trade.Ware;
import com.hml.mall.entity.trade.WareAttr;
import com.hml.mall.entity.trade.WareJj;
import com.hml.mall.entity.trade.WarePara;
import com.hml.mall.entity.trade.WareSc;
import com.hml.mall.entity.trade.WareXhys;
import com.hml.mall.iface.trade.IWareService;
import com.hml.mall.mapper.trade.BaseparaMapper;
import com.hml.mall.mapper.trade.WareAttrMapper;
import com.hml.mall.mapper.trade.WareJjMapper;
import com.hml.mall.mapper.trade.WareMapper;
import com.hml.mall.mapper.trade.WareParaMapper;
import com.hml.mall.mapper.trade.WareScMapper;
import com.hml.mall.mapper.trade.WareXhysMapper;
import com.hml.utils.StringUtils;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hml
 * @since 2021-04-17
 */

@Service("wareService")
public class WareServiceImpl extends ServiceImpl<WareMapper, Ware> implements IWareService {
	private Logger log = Logger.getLogger(WareServiceImpl.class);
	@Autowired
	private WareMapper wareMapper;
	
	@Autowired
	private WareScMapper wareScMapper;
	
	@Autowired
	private WareXhysMapper wareXhysMapper;
	
	@Autowired
	private WareJjMapper wareJjMapper;
	
	@Autowired
	private WareParaMapper wareParaMapper;
	
	@Autowired
	private BaseparaMapper baseparaMapper; 
	
	@Autowired
	private WareAttrMapper wareAttrMapper;
	
	@Override
	public Ware getById(Serializable id) {
		Ware ware = super.getById(id);
		if(ware == null) {
			return null;
		}
		String model = ware.getModule();
//		加载模式信息
		if(!StringUtils.isBlank(model)) {
			if(model.indexOf("A,") > -1) {
				WareSc sc = wareScMapper.selectById(id);
				ware.setWareSc(sc);
			}
			if(model.indexOf("B,") > -1) {
				WareXhys ys = wareXhysMapper.selectById(id);
				ware.setWareXhys(ys);
			}
			if(model.indexOf("C,") > -1) {
				WareJj jj = wareJjMapper.selectById(id);
				ware.setWareJj(jj);
			}
		}
		
//		加载属性信息
		QueryWrapper<WareAttr> attr = new QueryWrapper<WareAttr>();
		attr.eq("WARENO", id);
		List<WareAttr> attrs = wareAttrMapper.selectList(attr);
		if(attrs.size() <=0) {//商品未设置 加载大类属性
			attrs = wareAttrMapper.selectWareAttr(ware);
		}
		ware.setWareAttrs(attrs);
		return ware;
	}

    @Override
    public List< Ware> list(Ware model) {
       QueryWrapper< Ware> qw = new QueryWrapper();
       if(!StringUtils.isBlank(model.getSysblkno())) {
    	   qw.eq("sysblkno", model.getSysblkno());
       }
       List<Ware> list = wareMapper.findWare(qw);
       return list;
    }

    @Override
	public PageResult findPage(PageRequest pageRequest) {
		 
		return MybatisPlusPageHelper.findPage(pageRequest, wareMapper,"findPage");
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveWare(Ware ware) throws Exception {
		String model = "";
		String wareNo = ware.getWareno();
//		校验是否存在
		Ware one = wareMapper.selectById(wareNo);
		if(one != null) {
			throw new Exception("商品编号已存在！");
		}
//		保存商品模式信息
		System.out.println(ware);
		if(ware.getWareSc() != null) {//商城模式
			WareSc wareSc = ware.getWareSc();
			if(!StringUtils.isBlank(wareSc.getTitle())) {
				model = "A,";
				wareSc.setWareno(wareNo);
				wareSc.setCno(ware.getCno());
				wareScMapper.insert(wareSc);
			}
		}
		if(ware.getWareXhys() !=null) {//预售模式
			model += "B,";
			WareXhys ys = ware.getWareXhys();
			ys.setWareno(wareNo);
			ys.setCno(ware.getCno());
			wareXhysMapper.insert(ys);
		}
		if(ware.getWareJj() !=null) {//预售模式
			WareJj ys = ware.getWareJj();
			if(!StringUtils.isBlank(ys.getTitle())) {
				model += "C,";
				ys.setWareno(wareNo);
				ys.setCno(ware.getCno());
				wareJjMapper.insert(ys);
			}
		}
//		保存商品属性信息
		if(ware.getWareAttrs() !=null) {
			List<WareAttr> attrs = ware.getWareAttrs();
			for(WareAttr attr:attrs) {
				attr.setWareno(wareNo);
				wareAttrMapper.insert(attr);
			}
		}
//		保存商品参数信息
		if(ware.getWareParas() != null) {
			List<WarePara> paras = ware.getWareParas();
			for(WarePara para : paras) {
				para.setWareno(wareNo);
				wareParaMapper.insert(para);
			}
		}
//		保存商品信息
		ware.setModule(model);
		wareMapper.insert(ware);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateWare(Ware ware) throws Exception {
//		删除商品
		delWare(ware);
//		更新商品信息
		saveWare(ware);
		
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delWare(Ware ware) throws Exception {
		String wareNo = ware.getWareno();
//		删除商品模式信息
		wareScMapper.deleteById(wareNo);
		wareXhysMapper.deleteById(wareNo);
		wareJjMapper.deleteById(wareNo);
		QueryWrapper<WareAttr> attr = new QueryWrapper<WareAttr>();
		attr.eq("WARENO", wareNo);
//		删除商品属性信息
		wareAttrMapper.delete(attr);
//		删除商品参数信息
		QueryWrapper<WarePara> para = new QueryWrapper<WarePara>();
		para.eq("WARENO", wareNo);
		wareParaMapper.delete(para);
//		删除商品信息
		wareMapper.deleteById(wareNo);
	}
	
	@Override
	public List<WareAttr> findWareAttr(Ware ware) throws Exception {
		log.info("查询商品信息：" + ware.toString());
		if(StringUtils.isBlank(ware.getWareno())) {
			throw new Exception("商品编号不能为空");
		}
		QueryWrapper<WareAttr> qw = new QueryWrapper<WareAttr>();
		qw.eq("WARENO", ware.getWareno());
		List<WareAttr> list = wareAttrMapper.selectList(qw);
		return list;
	}
	
	@Override
	public List<Basepara> findWareParas(Ware ware) throws Exception {
		log.info("查询商品参数信息:" + ware);
		QueryWrapper<Basepara> qw = new QueryWrapper<Basepara>();
		qw.eq("PARATYPE","tb_jy_ware");
		List<Basepara> list = baseparaMapper.selectList(qw);
		if(ware != null && !StringUtils.isBlank(ware.getWareno())) {
			qw.eq("WARENO", ware.getWareno());
			List<Basepara> paraList = baseparaMapper.findBasePara(qw);
			if(paraList != null && paraList.size() > 0) {
				list = paraList;
			}
		}
//		格式化数据
		for(Basepara para: list) {
			if(!StringUtils.isBlank(para.getListData())) {
				String[] sels = para.getListData().split("\\|");
				for(String s : sels) {
					if(StringUtils.isBlank(s)) continue;
					String split = "";
					if(s.contains("=")) {
						split = "=";
					}else if(s.contains("、")) {
						split = "、";
					}else {
						log.error("未知分隔符:" + s);
						continue;
					}
					String[] opt = s.split(split);
					if(opt.length == 2) {
						Map<String,String> map = new HashMap<String,String>();
						map.put("key",opt[0]);
						map.put("val",opt[1]);
						para.getItems().add(map);
					}
				}
			}
		}
		return list;
	}
	/*
	 * 查询商城商品列表，过滤盲盒商品
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public List<WareSc> findWareSc(WareSc ware) throws Exception {
		QueryWrapper<WareSc> qw = new QueryWrapper<WareSc>();
		if(ware == null){
			qw.ne("FLAG", 4);
		}
		List<WareSc> list = wareScMapper.selectList(qw);
		return list;
	}
}

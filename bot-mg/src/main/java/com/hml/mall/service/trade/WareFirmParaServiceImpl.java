package com.hml.mall.service.trade;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hml.core.http.HttpResult;
import com.hml.core.page.MybatisPlusPageHelper;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.trade.Basepara;
import com.hml.mall.entity.trade.Ware;
import com.hml.mall.entity.trade.WareFirmPara;
import com.hml.mall.iface.trade.IWareFirmParaService;
import com.hml.mall.mapper.trade.BaseparaMapper;
import com.hml.mall.mapper.trade.WareFirmParaMapper;
import com.hml.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hml
 * @since 2022-07-18
 */

@Service("wareFirmParaService")
@Slf4j
public class WareFirmParaServiceImpl extends ServiceImpl<WareFirmParaMapper, WareFirmPara> implements IWareFirmParaService {

	@Autowired
	private WareFirmParaMapper wareFirmParaMapper;
	
	@Autowired
	private BaseparaMapper baseparaMapper; 

    @Override
    public List< WareFirmPara> list(WareFirmPara model) {
       QueryWrapper< WareFirmPara> wrapper = new QueryWrapper();
       return super.list(wrapper);
       
    }

    @Override
	public PageResult findPage(PageRequest pageRequest) {
		 
		return MybatisPlusPageHelper.findPage(pageRequest, wareFirmParaMapper,"findPage");
	}
    
    @Override
	public List<Basepara> findWareParas(WareFirmPara item) throws Exception {
		log.info("查询客户商品参数信息:" + item);
		QueryWrapper<Basepara> qw = new QueryWrapper<Basepara>();
		qw.eq("PARATYPE","tb_jy_ware_firm_para");
		List<Basepara> list = baseparaMapper.selectList(qw);
		if(item != null && !StringUtils.isBlank(item.getWareno())) {
			qw.eq("WARENO", item.getWareno());
			qw.eq("USERNO", item.getUserno());
			List<Basepara> paraList = baseparaMapper.findBaseFirmPara(qw);
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

	@Override
	@Transactional(rollbackFor = Exception.class)
	public HttpResult savePara(WareFirmPara item) throws Exception {
		String[] usernos = item.getUserno().split(",");
		String[] warenos = item.getWareno().split(",");
		List<Basepara> paras = item.getItems();
		for(String userno : usernos) {
			if(StringUtils.isBlank(usernos)) continue;
			for(String wareno : warenos) {
				if(StringUtils.isBlank(warenos)) continue;
				QueryWrapper<WareFirmPara> qw = new QueryWrapper<WareFirmPara>();
				qw.eq("USERNO", userno); 
				qw.eq("WARENO", wareno); 
				wareFirmParaMapper.delete(qw);
				for(Basepara para : paras) {
					WareFirmPara model = new WareFirmPara();
					model.setUserno(userno);
					model.setWareno(wareno);
					model.setParano(para.getParano());
					model.setParaval(para.getParaval());
					wareFirmParaMapper.insert(model);
				}
			}
		}
		return HttpResult.ok();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public HttpResult updatePara(WareFirmPara item) throws Exception {
		deletePara(item);
		savePara(item);
		return HttpResult.ok();
	}
	@Override
	@Transactional(rollbackFor = Exception.class)
	public HttpResult deletePara(WareFirmPara item) throws Exception {
		QueryWrapper<WareFirmPara> qw = new QueryWrapper<WareFirmPara>();
		qw.eq("USERNO", item.getUserno()); 
		qw.eq("WARENO", item.getWareno()); 
		wareFirmParaMapper.delete(qw);
		return HttpResult.ok();
	}

    
}

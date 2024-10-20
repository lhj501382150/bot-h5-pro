package com.hml.mall.service.trade;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hml.backcore.config.BackCoreConfig;
import com.hml.backcore.req.REQ517;
import com.hml.backcore.utils.ZbUtils;
import com.hml.core.page.MybatisPlusPageHelper;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.trade.WareMem;
import com.hml.mall.iface.trade.IWareMemService;
import com.hml.mall.mapper.trade.WareMemMapper;
import com.hml.mall.util.HttpUtils;
import com.hml.mall.util.IPUtils;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hml
 * @since 2022-07-27
 */

@Service("wareMemService")
public class WareMemServiceImpl extends ServiceImpl<WareMemMapper, WareMem> implements IWareMemService {

	@Autowired
	private WareMemMapper wareMemMapper;

    @Override
    public List< WareMem> list(WareMem model) {
       QueryWrapper< WareMem> wrapper = new QueryWrapper();
       return super.list(wrapper);
    }

    @Override
	public PageResult findPage(PageRequest pageRequest) {
		 
		return MybatisPlusPageHelper.findPage(pageRequest, wareMemMapper,"findPage");
	}

    @Override
    @Transactional(rollbackFor = Exception.class)
	public void updateStatus(WareMem item) throws Exception {
    	WareMem model = wareMemMapper.selectById(item.getWareno());
    	if(model == null) {
    		wareMemMapper.insert(item);
    	}else {
    		wareMemMapper.updateById(item);
    	}
		HttpServletRequest request = HttpUtils.getHttpServletRequest();
		REQ517 req = new REQ517();
		req.setLOGINNO("");
		req.setUSERNO("");
		req.setDETYPE("4");
		req.setDEVICENO("");
		req.setIP(IPUtils.getIpAddr(request));
		req.setWARENO(item.getWareno());
		req.setHSTATUS(item.getHstatus());
		req.setNUM(item.getNum());
		req.setSALNUM(item.getSalnum());
		
		ZbUtils.post(BackCoreConfig.WARE_CONTROL, req);
		
	}
}

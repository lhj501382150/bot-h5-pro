package com.hml.mall.service.trade;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hml.backcore.config.BackCoreConfig;
import com.hml.backcore.req.REQ514;
import com.hml.backcore.utils.ZbUtils;
import com.hml.core.page.MybatisPlusPageHelper;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.trade.WareScHandorder;
import com.hml.mall.iface.trade.IWareScHandorderService;
import com.hml.mall.mapper.trade.WareScHandorderMapper;
import com.hml.mall.util.HttpUtils;
import com.hml.mall.util.IPUtils;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hml
 * @since 2022-05-13
 */

@Service("wareScHandorderService")
public class WareScHandorderServiceImpl extends ServiceImpl<WareScHandorderMapper, WareScHandorder> implements IWareScHandorderService {

	@Autowired
	private WareScHandorderMapper wareScHandorderMapper;

    @Override
    public List< WareScHandorder> list(WareScHandorder model) {
       QueryWrapper< WareScHandorder> wrapper = new QueryWrapper();
       return super.list(wrapper);
    }

    @Override
	public PageResult findPage(PageRequest pageRequest) {
		 
		return MybatisPlusPageHelper.findPage(pageRequest, wareScHandorderMapper,"findPage");
	}

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void audit(WareScHandorder model) throws Exception {
    	 WareScHandorder item = wareScHandorderMapper.selectById(model.getHid());
    	 if(!"0".equals(item.getReqstatus())){
    		 throw new Exception("该数据已审核！");
    	 }
    	
    	 item.setReqstatus("1");
    	 
    	 wareScHandorderMapper.updateById(item);
    	 HttpServletRequest request = HttpUtils.getHttpServletRequest();
    	 REQ514 req = new REQ514();
    	 req.setLOGINNO(item.getSuserno());
     	 req.setUSERNO(item.getSuserno());
     	 req.setDETYPE("4");
     	 req.setDEVICENO("");
     	 req.setIP(IPUtils.getIpAddr(request));
     	 
     	 req.setWARENO(item.getWareno());
     	 req.setPRICE(String.valueOf(item.getPrice()));
     	 req.setNUM(String.valueOf(item.getNum()));
     	 req.setBS(item.getBs());
     	 req.setMODE("2");
     	 req.setEXT(item.getExt());
     	 req.setARTID(String.valueOf(item.getArtid()));
     	 
     	 ZbUtils.post(BackCoreConfig.TRADE514, req);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reject(WareScHandorder model) throws Exception {
    	WareScHandorder item = wareScHandorderMapper.selectById(model.getHid());
   	 if(!"0".equals(item.getReqstatus())){
   		 throw new Exception("该数据已审核！");
   	 }
   	
   	 item.setReqstatus("2");
   	 item.setMem(model.getMem());
   	wareScHandorderMapper.updateById(item);
    	
    }
}

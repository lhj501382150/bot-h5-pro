package com.hml.mall.service.game;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hml.backcore.utils.ZbUtils;
import com.hml.core.page.MybatisPlusPageHelper;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.game.PlayOrder;
import com.hml.mall.iface.game.IPlayOrderService;
import com.hml.mall.mapper.game.PlayOrderMapper;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hml
 * @since 2021-07-11
 */

@Service("playOrderService")
public class PlayOrderServiceImpl extends ServiceImpl<PlayOrderMapper, PlayOrder> implements IPlayOrderService {

	@Autowired
	private PlayOrderMapper playOrderMapper;

    @Override
    public List< PlayOrder> list(PlayOrder model) {
       QueryWrapper< PlayOrder> wrapper = new QueryWrapper();
       return super.list(wrapper);
    }

    @Override
	public PageResult findPage(PageRequest pageRequest) {
		 
		return MybatisPlusPageHelper.findPage(pageRequest, playOrderMapper,"findPage");
	}

    @Override
    @Transactional(rollbackFor =  Exception.class)
    public void sendPlayOrder(PlayOrder model) throws Exception {
    	PlayOrder order = playOrderMapper.selectById(model.getWaterno());
    	if(!"1".equals(order.getGmstatus())) {
    		throw new Exception("请确认订单已使用！");
    	} 
    	ZbUtils.confirmPlayOrder(order.getUserno(), String.valueOf(order.getWaterno()),model.getOptType());
    	
    }
}

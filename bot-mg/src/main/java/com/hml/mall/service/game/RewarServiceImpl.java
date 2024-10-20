package com.hml.mall.service.game;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hml.core.page.MybatisPlusPageHelper;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.game.Rewar;
import com.hml.mall.iface.game.IRewarService;
import com.hml.mall.mapper.game.RewarMapper;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hml
 * @since 2021-06-09
 */

@Service("rewarService")
public class RewarServiceImpl extends ServiceImpl<RewarMapper, Rewar> implements IRewarService {

	@Autowired
	private RewarMapper rewarMapper;

    @Override
    public List< Rewar> list(Rewar model) {
       QueryWrapper< Rewar> wrapper = new QueryWrapper();
       return super.list(wrapper);
    }

    @Override
	public PageResult findPage(PageRequest pageRequest) {
		 
		return MybatisPlusPageHelper.findPage(pageRequest, rewarMapper,"findPage");
	}

}

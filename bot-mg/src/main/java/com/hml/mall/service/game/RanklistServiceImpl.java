package com.hml.mall.service.game;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hml.core.page.MybatisPlusPageHelper;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.game.Ranklist;
import com.hml.mall.iface.game.IRanklistService;
import com.hml.mall.mapper.game.RanklistMapper;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hml
 * @since 2021-06-28
 */

@Service("ranklistService")
public class RanklistServiceImpl extends ServiceImpl<RanklistMapper, Ranklist> implements IRanklistService {

	@Autowired
	private RanklistMapper ranklistMapper;

    @Override
    public List< Ranklist> list(Ranklist model) {
       QueryWrapper< Ranklist> wrapper = new QueryWrapper();
       return super.list(wrapper);
    }

    @Override
	public PageResult findPage(PageRequest pageRequest) {
		 
		return MybatisPlusPageHelper.findPage(pageRequest, ranklistMapper);
	}
    
    @Override
    @Transactional(rollbackFor =  Exception.class)
	public boolean updateById(Ranklist entity) {
		UpdateWrapper<Ranklist> uw = new UpdateWrapper<Ranklist>();
		uw.eq("ltype", entity.getLtype());
		uw.eq("cyctype", entity.getCyctype());
		
		ranklistMapper.update(entity, uw);
		return true;
	}
    

}

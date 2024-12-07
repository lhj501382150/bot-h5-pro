package com.hml.mall.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hml.core.page.MybatisPlusPageHelper;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.Draw;
import com.hml.mall.mapper.DrawMapper;
import com.hml.mall.service.IDrawService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hml
 * @since 2024-12-05
 */

@Service("drawService")
public class DrawServiceImpl extends ServiceImpl<DrawMapper, Draw> implements IDrawService {

	@Autowired
	private DrawMapper drawMapper;

    @Override
    public List< Draw> list(Draw model) {
       QueryWrapper< Draw> wrapper = new QueryWrapper();
       return super.list(wrapper);
    }

    @Override
	public PageResult findPage(PageRequest pageRequest) {
		 
		return MybatisPlusPageHelper.findPage(pageRequest, drawMapper);
	}
    
    @Override
    public Draw getLast(String mode) {
    	
    	return drawMapper.getLast(mode);
    }

}

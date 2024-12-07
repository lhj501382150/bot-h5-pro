package com.hml.mall.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hml.core.page.MybatisPlusPageHelper;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.DrawType;
import com.hml.mall.mapper.DrawTypeMapper;
import com.hml.mall.service.IDrawTypeService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hml
 * @since 2024-12-05
 */

@Service("drawTypeService")
public class DrawTypeServiceImpl extends ServiceImpl<DrawTypeMapper, DrawType> implements IDrawTypeService {

	@Autowired
	private DrawTypeMapper drawTypeMapper;

    @Override
    public List< DrawType> list(DrawType model) {
       QueryWrapper< DrawType> wrapper = new QueryWrapper();
       return super.list(wrapper);
    }

    @Override
	public PageResult findPage(PageRequest pageRequest) {
		 
		return MybatisPlusPageHelper.findPage(pageRequest, drawTypeMapper);
	}

}

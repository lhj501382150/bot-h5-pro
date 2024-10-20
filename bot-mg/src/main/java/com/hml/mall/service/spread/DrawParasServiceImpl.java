package com.hml.mall.service.spread;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hml.core.page.MybatisPlusPageHelper;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.spread.DrawParas;
import com.hml.mall.iface.spread.IDrawParasService;
import com.hml.mall.mapper.spread.DrawParasMapper;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hml
 * @since 2021-09-06
 */

@Service("drawParasService")
public class DrawParasServiceImpl extends ServiceImpl<DrawParasMapper, DrawParas> implements IDrawParasService {

	@Autowired
	private DrawParasMapper drawParasMapper;

    @Override
    public List< DrawParas> list(DrawParas model) {
       QueryWrapper< DrawParas> wrapper = new QueryWrapper();
       return super.list(wrapper);
    }

    @Override
	public PageResult findPage(PageRequest pageRequest) {
		 
		return MybatisPlusPageHelper.findPage(pageRequest, drawParasMapper);
	}

}

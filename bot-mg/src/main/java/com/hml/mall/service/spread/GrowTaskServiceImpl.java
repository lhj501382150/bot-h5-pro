package com.hml.mall.service.spread;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hml.core.page.MybatisPlusPageHelper;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.spread.GrowTask;
import com.hml.mall.iface.spread.IGrowTaskService;
import com.hml.mall.mapper.spread.GrowTaskMapper;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hml
 * @since 2021-09-06
 */

@Service("growTaskService")
public class GrowTaskServiceImpl extends ServiceImpl<GrowTaskMapper, GrowTask> implements IGrowTaskService {

	@Autowired
	private GrowTaskMapper growTaskMapper;

    @Override
    public List< GrowTask> list(GrowTask model) {
       QueryWrapper< GrowTask> wrapper = new QueryWrapper();
       return super.list(wrapper);
    }

    @Override
	public PageResult findPage(PageRequest pageRequest) {
		 
		return MybatisPlusPageHelper.findPage(pageRequest, growTaskMapper);
	}

}

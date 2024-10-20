package com.hml.mall.service.sys;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hml.core.page.MybatisPlusPageHelper;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.sys.H5Log;
import com.hml.mall.iface.sys.IH5LogService;
import com.hml.mall.mapper.sys.H5LogMapper;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hml
 * @since 2024-06-25
 */

@Service("h5LogService")
public class H5LogServiceImpl extends ServiceImpl<H5LogMapper, H5Log> implements IH5LogService {

	@Autowired
	private H5LogMapper h5LogMapper;

    @Override
    public List< H5Log> list(H5Log model) {
       QueryWrapper< H5Log> wrapper = new QueryWrapper();
       return super.list(wrapper);
    }

    @Override
	public PageResult findPage(PageRequest pageRequest) {
		 
		return MybatisPlusPageHelper.findPage(pageRequest, h5LogMapper);
	}

}

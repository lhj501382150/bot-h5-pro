package com.hml.mall.service.sys;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hml.core.page.MybatisPlusPageHelper;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.sys.Log;
import com.hml.mall.iface.sys.ILogService;
import com.hml.mall.mapper.sys.LogMapper;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hml
 * @since 2021-04-05
 */

@Service("logService")
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements ILogService {

	@Autowired
	private LogMapper logMapper;

    @Override
    public List< Log> list(Log model) {
       QueryWrapper< Log> wrapper = new QueryWrapper();
       return super.list(wrapper);
    }

	@Override
	public PageResult findPage(PageRequest pageRequest) {
		// TODO Auto-generated method stub
		return MybatisPlusPageHelper.findPage(pageRequest, logMapper);
	}

}

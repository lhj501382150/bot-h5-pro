package com.hml.mall.service.notice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hml.core.page.MybatisPlusPageHelper;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.notice.Mess;
import com.hml.mall.iface.notice.IMessService;
import com.hml.mall.mapper.notice.MessMapper;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hml
 * @since 2021-04-20
 */

@Service("messService")
public class MessServiceImpl extends ServiceImpl<MessMapper, Mess> implements IMessService {

	@Autowired
	private MessMapper messMapper;

    @Override
    public List<Mess> list(QueryWrapper<Mess> wrapper) {
       List<Mess> list = messMapper.findList(wrapper);
       return list;
    }

    @Override
	public PageResult findPage(PageRequest pageRequest) {
		 
		return MybatisPlusPageHelper.findPage(pageRequest, messMapper);
	}

}

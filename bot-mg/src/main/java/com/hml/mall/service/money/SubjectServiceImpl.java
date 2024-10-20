package com.hml.mall.service.money;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hml.core.page.MybatisPlusPageHelper;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.money.Subject;
import com.hml.mall.iface.money.ISubjectService;
import com.hml.mall.mapper.money.SubjectMapper;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hml
 * @since 2021-04-14
 */

@Service("subjectService")
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject> implements ISubjectService {

	@Autowired
	private SubjectMapper subjectMapper;

    @Override
    public List< Subject> list(Subject model) {
       QueryWrapper< Subject> wrapper = new QueryWrapper();
       return super.list(wrapper);
    }

    @Override
	public PageResult findPage(PageRequest pageRequest) {
		 
		return MybatisPlusPageHelper.findPage(pageRequest, subjectMapper);
	}

}

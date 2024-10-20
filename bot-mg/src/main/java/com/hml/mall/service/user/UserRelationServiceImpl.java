package com.hml.mall.service.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hml.core.page.MybatisPlusPageHelper;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.user.UserRelation;
import com.hml.mall.iface.user.IUserRelationService;
import com.hml.mall.mapper.user.UserRelationMapper;
import com.hml.utils.StringUtils;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hml
 * @since 2021-04-11
 */

@Service("userRelationService")
public class UserRelationServiceImpl extends ServiceImpl<UserRelationMapper, UserRelation> implements IUserRelationService {

	@Autowired
	private UserRelationMapper userRelationMapper;

    @Override
    public List<UserRelation> list(UserRelation model) {
       QueryWrapper< UserRelation> wrapper = new QueryWrapper();
       return super.list(wrapper);
    }

    @Override
	public PageResult findPage(PageRequest pageRequest) {
		 
		return MybatisPlusPageHelper.findPage(pageRequest, userRelationMapper);
	}

}

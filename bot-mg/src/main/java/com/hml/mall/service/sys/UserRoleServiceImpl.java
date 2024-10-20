package com.hml.mall.service.sys;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hml.mall.entity.sys.UserRole;
import com.hml.mall.iface.sys.IUserRoleService;
import com.hml.mall.mapper.sys.UserRoleMapper;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hml
 * @since 2021-04-05
 */

@Service("userRoleService")
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {


    @Override
    public List< UserRole> list(UserRole model) {
       QueryWrapper< UserRole> wrapper = new QueryWrapper();
       return super.list(wrapper);
    }

    @Override
    public IPage< UserRole> pageList( UserRole model, Integer pageNum, Integer pageSize) {

       QueryWrapper< UserRole> wrapper = new QueryWrapper(model);
       return this.page(new Page<>(pageNum, pageSize), wrapper);
    }


}

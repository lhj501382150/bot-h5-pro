package com.hml.mall.service.sys;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hml.mall.entity.sys.RoleMenu;
import com.hml.mall.iface.sys.IRoleMenuService;
import com.hml.mall.mapper.sys.RoleMenuMapper;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hml
 * @since 2021-04-05
 */

@Service("roleMenuService")
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements IRoleMenuService {


    @Override
    public List< RoleMenu> list(RoleMenu model) {
       QueryWrapper< RoleMenu> wrapper = new QueryWrapper();
       return super.list(wrapper);
    }

    @Override
    public IPage< RoleMenu> pageList( RoleMenu model, Integer pageNum, Integer pageSize) {

       QueryWrapper< RoleMenu> wrapper = new QueryWrapper(model);
       return this.page(new Page<>(pageNum, pageSize), wrapper);
    }


}

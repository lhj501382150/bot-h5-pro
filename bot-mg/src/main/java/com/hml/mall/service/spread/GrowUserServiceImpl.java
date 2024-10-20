package com.hml.mall.service.spread;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hml.core.page.MybatisPlusPageHelper;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.spread.GrowUser;
import com.hml.mall.iface.spread.IGrowUserService;
import com.hml.mall.mapper.spread.GrowUserMapper;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hml
 * @since 2021-09-06
 */

@Service("growUserService")
public class GrowUserServiceImpl extends ServiceImpl<GrowUserMapper, GrowUser> implements IGrowUserService {

	@Autowired
	private GrowUserMapper growUserMapper;

    @Override
    public List< GrowUser> list(GrowUser model) {
       QueryWrapper< GrowUser> wrapper = new QueryWrapper();
       return super.list(wrapper);
    }

    @Override
	public PageResult findPage(PageRequest pageRequest) {
		 
		return MybatisPlusPageHelper.findPage(pageRequest, growUserMapper,"findPage");
	}

}

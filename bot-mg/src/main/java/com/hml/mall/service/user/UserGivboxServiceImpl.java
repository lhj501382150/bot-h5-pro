package com.hml.mall.service.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hml.core.page.MybatisPlusPageHelper;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.user.UserGivbox;
import com.hml.mall.iface.user.IUserGivboxService;
import com.hml.mall.mapper.user.UserGivboxMapper;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hml
 * @since 2022-10-28
 */

@Service("userGivboxService")
public class UserGivboxServiceImpl extends ServiceImpl<UserGivboxMapper, UserGivbox> implements IUserGivboxService {

	@Autowired
	private UserGivboxMapper userGivboxMapper;

    @Override
    public List< UserGivbox> list(UserGivbox model) {
       QueryWrapper< UserGivbox> wrapper = new QueryWrapper();
       return super.list(wrapper);
    }

    @Override
	public PageResult findPage(PageRequest pageRequest) {
		 
		return MybatisPlusPageHelper.findPage(pageRequest, userGivboxMapper);
	}

}

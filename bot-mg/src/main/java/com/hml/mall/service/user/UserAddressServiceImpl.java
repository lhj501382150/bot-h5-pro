package com.hml.mall.service.user;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hml.core.page.MybatisPlusPageHelper;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.user.UserAddress;
import com.hml.mall.iface.user.IUserAddressService;
import com.hml.mall.mapper.user.UserAddressMapper;
import com.hml.utils.StringUtils;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hml
 * @since 2021-04-11
 */

@Service("userAddressService")
public class UserAddressServiceImpl extends ServiceImpl<UserAddressMapper, UserAddress> implements IUserAddressService {

	@Autowired
	private UserAddressMapper userAddressMapper;

    @Override
    public List< UserAddress> list(UserAddress model) {
       QueryWrapper< UserAddress> wrapper = new QueryWrapper();
       if(model!=null) {
    	   if(!StringUtils.isBlank(model.getUserno())) {
    		   wrapper.eq("USERNO", model.getUserno());
    	   }
       }
       return super.list(wrapper);
    }

    @Override
	public PageResult findPage(PageRequest pageRequest) {
		 
		return MybatisPlusPageHelper.findPage(pageRequest, userAddressMapper);
	}

}

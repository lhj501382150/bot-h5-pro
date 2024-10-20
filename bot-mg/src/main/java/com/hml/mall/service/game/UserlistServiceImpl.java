package com.hml.mall.service.game;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hml.core.page.MybatisPlusPageHelper;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.game.Userlist;
import com.hml.mall.iface.game.IUserlistService;
import com.hml.mall.mapper.game.UserlistMapper;
import com.hml.mall.security.LoginUser;
import com.hml.mall.util.SecurityUtils;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hml
 * @since 2021-06-09
 */

@Service("userlistService")
public class UserlistServiceImpl extends ServiceImpl<UserlistMapper, Userlist> implements IUserlistService {

	@Autowired
	private UserlistMapper userlistMapper;

    @Override
    public List< Userlist> list(Userlist model) {
       QueryWrapper< Userlist> wrapper = new QueryWrapper();
       if (model.getActno() != null) {
    	   wrapper.eq("ACTNO",model.getActno());
       }
       if(model.getUserno() != null) {
    	   wrapper.eq("USERNO", model.getUserno());
       }
       wrapper.orderByAsc("SEQ");
       return super.list(wrapper);
    }

    @Override
	public PageResult findPage(PageRequest pageRequest) {
    	LoginUser loginUser = SecurityUtils.getLoginInfo();
    	if(loginUser.getType() > 0){
    		String key = loginUser.getQueryNo();
    		 pageRequest.getParams().put(key, loginUser.getUserno());
    	}
		return MybatisPlusPageHelper.findPage(pageRequest, userlistMapper,"findPage");
	}

   @Override
	public boolean updateById(Userlist entity) {
	   UpdateWrapper<Userlist> uw = new UpdateWrapper<Userlist>();
	   uw.eq("ACTNO", entity.getActno());
	   uw.eq("USERNO", entity.getUserno());
	 userlistMapper.update(entity, uw);
	 return true;
		
	}
}

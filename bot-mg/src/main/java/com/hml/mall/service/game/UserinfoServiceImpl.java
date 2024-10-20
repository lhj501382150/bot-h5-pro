package com.hml.mall.service.game;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hml.core.page.MybatisPlusPageHelper;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.game.Userinfo;
import com.hml.mall.iface.game.IUserinfoService;
import com.hml.mall.mapper.game.UserinfoMapper;
import com.hml.mall.security.LoginUser;
import com.hml.mall.util.SecurityUtils;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hml
 * @since 2021-06-28
 */

@Service("userinfoService")
public class UserinfoServiceImpl extends ServiceImpl<UserinfoMapper, Userinfo> implements IUserinfoService {

	@Autowired
	private UserinfoMapper userinfoMapper;

    @Override
    public List< Userinfo> list(Userinfo model) {
       QueryWrapper< Userinfo> wrapper = new QueryWrapper();
       return super.list(wrapper);
    }

    @Override
	public PageResult findPage(PageRequest pageRequest) {
    	LoginUser loginUser = SecurityUtils.getLoginInfo();
    	if(loginUser.getType() > 0){
    		String key = loginUser.getQueryNo();
    		 pageRequest.getParams().put(key, loginUser.getUserno());
    	}
		return MybatisPlusPageHelper.findPage(pageRequest, userinfoMapper,"findPage");
	}

}

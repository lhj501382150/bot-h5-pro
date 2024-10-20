package com.hml.mall.service.bot;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hml.core.page.MybatisPlusPageHelper;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.bot.ChatUser;
import com.hml.mall.iface.bot.IChatUserService;
import com.hml.mall.mapper.bot.ChatUserMapper;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hml
 * @since 2023-12-08
 */

@Service("chatUserService")
public class ChatUserServiceImpl extends ServiceImpl<ChatUserMapper, ChatUser> implements IChatUserService {

	@Autowired
	private ChatUserMapper chatUserMapper;

    @Override
    public List< ChatUser> list(ChatUser model) {
       QueryWrapper< ChatUser> wrapper = new QueryWrapper();
       return super.list(wrapper);
    }

    @Override
	public PageResult findPage(PageRequest pageRequest) {
		 
		return MybatisPlusPageHelper.findPage(pageRequest, chatUserMapper);
	}

}

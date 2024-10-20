package com.hml.runner;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.cache.CacheProperties.Redis;
import org.springframework.stereotype.Component;

import com.hml.mall.entity.bot.Chat;
import com.hml.mall.iface.bot.IChatService;
import com.hml.mall.iface.user.IUserService;
import com.hml.redis.RedisKey;
import com.hml.redis.RedisUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DataInit implements ApplicationRunner{

	@Autowired
	private IChatService chatService;
	
	@Autowired
	private RedisUtils redisUtils;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		 
		List<Chat> chats = chatService.list();
		if(chats != null){
			for(Chat item : chats){
				redisUtils.set(RedisKey.ROB_STATUS + item.getChatid(), item.getStatus());
				log.info("初始化机器人状态：{}-【{}】",RedisKey.ROB_STATUS + item.getChatid(), item.getStatus());
			}
		}
		
//		userService.initRelation();
	}

}

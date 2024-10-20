package com.hml.command;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.hml.bean.MsgDTO;
import com.hml.bot.BaseBot;
import com.hml.redis.RedisUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("errorCommand")
public class ErrorCommand extends BaseBtn {
	
	@Autowired
	private RedisUtils redisUtils;

	@SuppressWarnings("unused")
	public void sendError(BaseBot bot,Long who, String msg, Message message) {
 		 SendMessage sm = null;
 		long num = 0;
 		 if(message != null) {
 			 User user = message.getFrom();
 			String redisKey = "ERROR:" + user.getUserName();
 	 		num = redisUtils.incr(redisKey, 1);
 			sm = SendMessage.builder().chatId(who.toString())
				    .replyToMessageId(message.getMessageId())
		            .parseMode("Markdown")
		            .text(msg)
		            .build();
 		 }else {
 			sm = SendMessage.builder().chatId(who.toString())
		            .parseMode("Markdown")
		            .text(msg)
		            .build();
 		 }
		 try {
			bot.execute(sm);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
		 if(num > 10) {//超过十次禁言
			 
		 }
 
	}
	 
	
}

package com.hml.command;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.groupadministration.ExportChatInviteLink;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.User;

import com.hml.back.bean.RespMoney;
import com.hml.back.service.BackCoreService;
import com.hml.bot.BaseBot;
import com.hml.config.BotConfig;
import com.hml.redis.RedisKey;
import com.hml.redis.RedisUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("callBackCommand")
public class CallBackCommand extends BaseBtn {
	
	@Autowired
	private BackCoreService backCoreService;
	
	@Autowired
	private RedisUtils redisUtils;
	
	public void queryYe(BaseBot bot,CallbackQuery query) {
		try {
			User user = query.getFrom();
			RespMoney money = backCoreService.queryMoney(user);
			StringBuffer buff = new StringBuffer();
			buff.append("\t\t\t\t当前余额               ").append("\n");
			buff.append("----------------------------------------------").append("\n");
			buff.append("\t\tID：").append(user.getId()).append("\n");
			buff.append("\t\t余额：").append(money.getEnable()).append("\n");
			buff.append("\t\t冻结：").append(money.getFreeze()).append("\n");
			log.info("【查询余额】{}-{}",user.getUserName(),buff.toString());
			AnswerCallbackQuery close = AnswerCallbackQuery.builder()
		            .callbackQueryId(query.getId())
		            .text(buff.toString())
		            .showAlert(true)
		            .build();
			bot.execute(close);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	public void queryJRBB(BaseBot bot,CallbackQuery query) {
		try {
			AnswerCallbackQuery close = AnswerCallbackQuery.builder()
					.callbackQueryId(query.getId())
					.text("今日报表")
					.showAlert(true)
					.build();
			bot.execute(close);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	public void queryTGCX(BaseBot bot,CallbackQuery query) {
		try {
			AnswerCallbackQuery close = AnswerCallbackQuery.builder()
					.callbackQueryId(query.getId())
					.text("推广查询")
					.showAlert(true)
					.build();
			bot.execute(close);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	public void queryTGLJ(BaseBot bot,CallbackQuery query) {
		try {
			User user = query.getFrom();
			String userId = String.valueOf(user.getId());
			Object val = redisUtils.hget(RedisKey.USER_INVITE_AUTH, userId);
			log.info("查询邀请链接权限:{}-【{}】",userId,val);
			String text = "";
			if(val != null && "1".equals(val.toString())) {
				ExportChatInviteLink inviteLink = new ExportChatInviteLink();
				inviteLink.setChatId(String.valueOf(BotConfig.CHAT_ID));
				String execute = bot.execute(inviteLink) ;
				redisUtils.hset(RedisKey.CHAT_INVITE_LINK, userId, execute);
				String botName = bot.getBotUsername();
				String link = "https://t.me/"+botName + "?start="+userId;
				text = "@"+user.getUserName() +"您的推广链接是：" +link;
			}else {
				text = "您暂无权限获取邀请链接，如有疑问请联系管理员";
			}
			
			SendMessage sm = SendMessage.builder()
                    .chatId(String.valueOf(BotConfig.CHAT_ID)) //Who are we sending a message to
                    .text(text).build(); 
			bot.execute(sm);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

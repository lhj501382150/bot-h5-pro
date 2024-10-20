package com.hml.command;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.alibaba.fastjson.JSONObject;
import com.hml.back.service.BackCoreService;
import com.hml.bean.MsgDTO;
import com.hml.bot.BaseBot;
import com.hml.config.BotConfig;
import com.hml.task.DrawInfo;
import com.hml.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("bankerCommand")
public class BankerCommand extends BaseBtn {
	
//	三方服务
	@Autowired
	private BackCoreService backCoreService;

	public void sendQZ(BaseBot bot,Long who, MsgDTO msg, Message message) {
		User user = message.getFrom();
		String userName = getUserName(user);
		JSONObject req = new JSONObject();
		req.put("botId", String.valueOf(BotConfig.CHAT_ID));
		req.put("dataId", DrawInfo.ID);
		req.put("userId", String.valueOf(user.getId()));
		req.put("userName", userName);
		req.put("integRal", msg.getMoney());
		try {
			JSONObject res = backCoreService.robOrder(req);
			StringBuffer buff = new StringBuffer();
			buff.append(String.format("恭喜%s抢庄成功！抢庄金额：(%s-%s)", userName,DrawInfo.MIN_MONEY,DrawInfo.MAX_MONEY)).append("\n");
			buff.append(String.format("当前庄主：%s", userName)).append("\n");
			buff.append(String.format("庄主金额：%s", res.getString("integeRal"))).append("\n");
			SendPhoto photo = getSendPhoto(who, buff.toString(), "Markdown");
			try {
				bot.execute(photo);
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			SendMessage sendMessage = getSendMessage(who,  e.getMessage() + "，如果10次输入错误，则采取禁言处理", "Markdown", message);
			try {
				bot.execute(sendMessage);
			} catch (TelegramApiException e1) {
				e.printStackTrace();
			}
		}
	}
	
	public void sendXZ(BaseBot bot,Long who, MsgDTO msg, Message message) {
 		
		User user = message.getFrom();
		JSONObject req = new JSONObject();
		req.put("botId", String.valueOf(BotConfig.CHAT_ID));
		req.put("dataId", DrawInfo.ID);
		req.put("userId", String.valueOf(user.getId()));
		req.put("userName", getUserName(user)); 
		String res = backCoreService.downRob(req);
		
		if("ok".equals(res)) {
			StringBuffer buff = new StringBuffer();
			buff.append("下庄成功！从下轮起，重新竞选庄家，请广大玩家踊跃参加庄家竞选。");
			SendPhoto photo = getSendPhoto(who, buff.toString(), "Markdown");
			try {
				bot.execute(photo);
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}
		}else {//抢失败处理
			SendMessage sendMessage = getSendMessage(who, res + "，如果10次输入错误，则采取禁言处理", "Markdown", message);
			try {
				bot.execute(sendMessage);
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}
		}
	}
	 
	public void sendXZHU(BaseBot bot,Long who, MsgDTO msg, Message message) {
		User user = message.getFrom();
		JSONObject req = new JSONObject();
		String userName = getUserName(user);
		req.put("botId", String.valueOf(BotConfig.CHAT_ID));
		req.put("dataId", DrawInfo.ID);
		req.put("userId", String.valueOf(user.getId()));
		req.put("userName", userName); 
		req.put("integRal", msg.getMoney());
		req.put("ruId", msg.getCommand());
		try {
			JSONObject ret = backCoreService.order(req);
			if(ret != null) {
				StringBuffer buff = new StringBuffer();
				buff.append(String.format("%s下注成功！", userName,user.getId())).append("\n");
				buff.append(String.format("下注金额：%d", msg.getMoney())).append("\n");
				buff.append(String.format("下注类型：%s(%s)", msg.getCommand(),msg.getNum())).append("\n");
				buff.append(String.format("庄家：%s", ret.getString("userName"))).append("\n");
				buff.append(String.format("庄家资产剩余：%s", ret.getString("integeRal"))).append("\n");
				buff.append(String.format("订单编号：%s", ret.getString("ordId"))).append("\n");
				buff.append(String.format("期数：%s", DrawInfo.DRAW_ISSUE)).append("\n");
				SendMessage photo = getSendMessage(who, buff.toString(), "Markdown");
				try {
					bot.execute(photo);
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			SendMessage sendMessage = getSendMessage(who, e.getMessage() + ",如果10次输入错误，则采取禁言处理", "Markdown", message);
			try {
				bot.execute(sendMessage);
			} catch (TelegramApiException e1) {
				e.printStackTrace();
			}
		}
		 
	}

	private String getUserName(User user) {
		String first = StringUtils.isBlank(user.getFirstName()) ? "" : user.getFirstName();
		String last = StringUtils.isBlank(user.getLastName()) ? "" : user.getLastName();
		return first + last;
	}
}

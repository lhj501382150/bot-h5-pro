package com.hml.bot;

import java.lang.reflect.Field;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.CopyMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.hml.back.service.BackCoreService;
import com.hml.bean.MsgCommand;
import com.hml.bean.MsgDTO;
import com.hml.command.BankerCommand;
import com.hml.command.BaseBtn;
import com.hml.command.BaseCommand;
import com.hml.command.CallBackCommand;
import com.hml.command.ErrorCommand;
import com.hml.config.BotCommand;
import com.hml.config.BotConfig;
import com.hml.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BaseBot extends TelegramLongPollingBot {
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired
	private BankerCommand bankerCommand;
	@Autowired
	private BackCoreService backCoreService;
	@Autowired
	private ErrorCommand errorCommand;
	
	@Autowired
	private CallBackCommand callBackCommand;
	
	@Override
	public void onUpdateReceived(Update update) {
//		printObj(update);
		if(update.hasCallbackQuery()) {//回调
			CallbackQuery query = update.getCallbackQuery();
			handleCallback(query);
		}else if(update.hasMessage()){//消息
			Message msg = update.getMessage();
			String text = msg.getText();
			System.out.println("------------------" + text);
			if(text != null && text.startsWith("/start") && text.length() > 8) {
				backCoreService.addInviteAccount(this, msg);
			}else {
				List<User> newChatMembers = msg.getNewChatMembers();
				if(newChatMembers != null && newChatMembers.size() > 0) {
					backCoreService.addAccount(newChatMembers);
				}
				User user = msg.getFrom();
				System.out.println(user);
				handleMessage(msg);
			}
			
		}
		
	}
	private void handleCallback(CallbackQuery query) {
		try {
			String data = query.getData();
			switch (data) {
			case BotCommand.YE:
				callBackCommand.queryYe(this, query);
				break;
			case BotCommand.TGCX:
				callBackCommand.queryTGCX(this, query);
				break;
			case BotCommand.TGLJ:
				callBackCommand.queryTGLJ(this, query);
				break;
			case BotCommand.JRBB:
				callBackCommand.queryJRBB(this, query);
				break;
			default:
				break;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void handleMessage(Message msg) {
		try {
			Chat chat = msg.getChat();
			
			System.out.println("CHATID : " + chat.getId());
			System.out.println("INVITE-LINK : " + chat.getInviteLink());
			if(!StringUtils.isBlank(BotConfig.CHAT_ID) &&  chat.getId() - BotConfig.CHAT_ID != 0 ) {
				//errorCommand.sendError(this, chat.getId(), "不是这个群，你不能用", msg);
				return;
			}
			if(!backCoreService.isBotEnable(String.valueOf(BotConfig.CHAT_ID))) {
//				errorCommand.sendError(this, BotConfig.CHAT_ID, "机器人休息了...", msg);
				return;
			}
		   Long chatid = chat.getId();
			if(msg.isCommand()) {
				handleCommand(msg,chatid);
			}else {
				handleMsgCommand(msg,chatid);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void handleMsgCommand(Message msg,Long chatid) {
		if(msg.getText() == null) return;
		String text = msg.getText().trim();
		if("下庄".equals(text)) {
			text += "1"; 
		}
		MsgDTO msgDTO = BaseBtn.parse(text);
		
		if(msgDTO.isCommand()) {
			 if(msgDTO.getCommand().equals(MsgCommand.QZ.getValue())) {
				 bankerCommand.sendQZ(this, chatid, msgDTO, msg);
			 }else if(msgDTO.getCommand().equals(MsgCommand.XZ.getValue())) {
				 bankerCommand.sendXZ(this, chatid, msgDTO, msg);
			 }else{
				 bankerCommand.sendXZHU(this, chatid, msgDTO, msg);
			 }
		}else {
			errorCommand.sendError(this, chatid, "指令错误，请参考玩法说明重新输入。如果10次输入错误，则采取禁言处理", msg);
		}
	}
	private void handleCommand(Message msg,Long chatid) throws TelegramApiException {
		String command = msg.getText();
		String beanId = command.substring(1) + "Command"; 
		boolean exist = applicationContext.containsBean(beanId);
		if(exist) {
			BaseCommand baseCommand = (BaseCommand)applicationContext.getBean(beanId);
			if(baseCommand != null) {
				SendPhoto photo = baseCommand.sendPhoto(chatid,"",msg);
				
				SendMessage message = baseCommand.sendMessage(chatid,"",msg);
				if(photo != null) {
					execute(photo);
				}
				if(message != null) {
					execute(message);
				}
			}
		}
	}
	
	@Override
	public String getBotUsername() {
		
		return BotConfig.USERNAME;
	}

	@Override
	public String getBotToken() {
		
		return BotConfig.TOKEN;
	}

	private <T> void printObj(T obj) {
		  try {
			Field[] fields = obj.getClass().getDeclaredFields();
			for(Field field : fields) {
				field.setAccessible(true);
				Object val = field.get(obj);
				if(val == null) continue;
				System.out.println("【"+field.getName()+"】：" + val);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	  }
	
	
	public void copyMessage(Long who, Integer msgId){
		  CopyMessage cm = CopyMessage.builder()
	             .fromChatId(who.toString())  //We copy from the user
	          .chatId(who.toString())      //And send it back to him
	          .messageId(msgId)            //Specifying what message
	          .build();
		   try {
		        execute(cm);                        //Actually sending the message
		   } catch (TelegramApiException e) {
		        throw new RuntimeException(e);      //Any error will be printed here
		   }
		}	
	
	
}

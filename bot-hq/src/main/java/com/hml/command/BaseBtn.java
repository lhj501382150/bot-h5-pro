package com.hml.command;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.core.io.ClassPathResource;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import com.hml.bean.MsgDTO;
import com.hml.config.BotCommand;
import com.hml.config.BotConfig;

public class BaseBtn {

	public SendMessage getSendMessage(Long who,String text,String mode,Message message) {
		SendMessage sm = SendMessage.builder().chatId(who.toString())
			    .replyToMessageId(message.getMessageId())
	            .parseMode(mode)
	            .text(text)
	            .build();
		return sm;
	}
	public SendMessage getSendMessage(Long who,String text,String mode) {
		SendMessage sm = SendMessage.builder().chatId(who.toString())
				.parseMode(mode)
				.text(text)
				.build();
		return sm;
	}
	
	public SendPhoto getSendPhoto(Long who,String text,String mode) {
		InputFile inputFile = getInputFile();
//		List<List<InlineKeyboardButton>> btns = getBtns();
//		InlineKeyboardMarkup markup = InlineKeyboardMarkup.builder().keyboard(btns).build();
		SendPhoto photo = SendPhoto.builder().chatId(who.toString())
		                   .parseMode(mode)
		                   .photo(inputFile)
//		                   .replyMarkup(markup)
		                   .caption(text)
		                   .build();
		return photo;
	}
	
	public List<List<InlineKeyboardButton>> getBtns(){
		List<List<InlineKeyboardButton>> rowBtns = new ArrayList<List<InlineKeyboardButton>>();
		 List<InlineKeyboardButton> row1 = new ArrayList<InlineKeyboardButton>();
		 List<InlineKeyboardButton> row2 = new ArrayList<InlineKeyboardButton>();
		 List<InlineKeyboardButton> row3 = new ArrayList<InlineKeyboardButton>();
		 List<InlineKeyboardButton> row4 = new ArrayList<InlineKeyboardButton>();
		 
		 row1.add(InlineKeyboardButton.builder().text("客服").url(BotConfig.KF_CHANNEL).build());
		 if(BotConfig.CZ_CHANNEL.contains(",")) {
			 String[] strs = BotConfig.CZ_CHANNEL.split(",");
			 for(int i = 0;i <strs.length ; i ++ ) {
				 row1.add(InlineKeyboardButton.builder().text("充值" + (i+1)).url(strs[i]).build());
			 }
		 }else {
			 row1.add(InlineKeyboardButton.builder().text("充值").url(BotConfig.CZ_CHANNEL).build());
		 }
		 row1.add(InlineKeyboardButton.builder().text("提现").url(BotConfig.TX_CHANNEL).build());
		
		 row2.add(InlineKeyboardButton.builder().text("玩法说明").url(BotConfig.WF_CHANNEL).build());
		 row2.add(InlineKeyboardButton.builder().text("余额").callbackData(BotCommand.YE).build());
		 row4.add(InlineKeyboardButton.builder().text("推广查询").callbackData(BotCommand.TGCX).build());
		 row4.add(InlineKeyboardButton.builder().text("推广链接").callbackData(BotCommand.TGLJ).build());
		 
		 row3.add(InlineKeyboardButton.builder().text("开奖链接").url(BotConfig.KJ_CHANNEL).build());
		 
		 rowBtns.add(row1);
		 rowBtns.add(row2);
		 rowBtns.add(row4);
		 rowBtns.add(row3);
		 
		 return rowBtns;
	}
	
	public InputFile getInputFile() {
		InputFile inputFile = null;
		try {
			InputStream input = new ClassPathResource("images/bg.jpg").getInputStream();
			inputFile = new InputFile(input,"bg.jpg");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return inputFile;
	}
	public InputFile getInputFile(Integer winNo) {
		InputFile inputFile = null;
		try {
			String fileName = "images/bg.jpg";
			if(winNo != null && winNo <=4 && winNo > 0) {
				fileName = "images/"+winNo+".jpg";
			}
			InputStream input = new ClassPathResource(fileName).getInputStream();
			inputFile = new InputFile(input,"bg.jpg");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return inputFile;
	}
	public static MsgDTO parse(String text) {
		try {
			text = text.trim().replaceAll("\\s+", "");
			// 使用正则表达式匹配所有的汉字和数字
	        Matcher m = Pattern.compile("[\\u4e00-\\u9fa5]+|[0-9]+").matcher(text);
	        if (m.find()) {
	            // 获取所有匹配到的汉字
	            String chineseStr = m.group();
	            // 剩余的就是数字了
	            String numStr = text.replace(chineseStr, "");
	            
	            if(Integer.valueOf(numStr) > 0) {
	            	return new MsgDTO(chineseStr, Integer.valueOf(numStr));
	            }
	        }
		} catch (Exception e) {
			System.out.println("指令错误：" + e.getMessage());
		}
        
        return new MsgDTO(text, false);
	}
	
	
}

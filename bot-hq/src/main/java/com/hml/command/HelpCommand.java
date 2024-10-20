package com.hml.command;


import java.util.List;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@Service("helpCommand")
public class HelpCommand extends BaseBtn implements BaseCommand{

	@Override
	public SendPhoto sendPhoto(Long who, String what, Message mesage) {
//		File file = new File("F:\\PPT\\20180110_IMG_0041.PNG");
//		InputFile inputFile = new InputFile(file, "bg.jpg");
		InputFile inputFile = getInputFile();
		String text = "<tg-emoji emoji-id=\"5368324170671202286\">👍</tg-emoji>四方宝斗 \n ";
		text += "\n<tg-emoji emoji-id=\"5368324170671202287\">🧨</tg-emoji>四方通宝 八方来财 <tg-emoji emoji-id=\"5368324170671202287\">🧨</tg-emoji>";
		text += "\n————————————— ";
		text += "\n目前已备好200万U为每天的启动资金，开奖以官网（澳洲幸运10）为准：公平 公正 公开 全国统一开\n";
		text += "\n<tg-emoji emoji-id=\"5368324170671202287\">🔔</tg-emoji>本群全天24小时营业 <tg-emoji emoji-id=\"5368324170671202287\">🔔</tg-emoji>";
		text += "\n欢迎各位老板闲暇之余娱乐放松";
		text += "\n<tg-emoji emoji-id=\"5368324170671202287\">🧧</tg-emoji>何以解忧，唯有暗宝 <tg-emoji emoji-id=\"5368324170671202287\">🧧</tg-emoji>";
		text += "\nVIP群组 ：https: \n";
		text += "\n统一VIP开房上下分私密   ";
		text += "\n本群永久庄家担保责任人 ";
		 List<List<InlineKeyboardButton>> btns = getBtns();
		 InlineKeyboardMarkup markup = InlineKeyboardMarkup.builder().keyboard(btns).build();
		 SendPhoto photo = SendPhoto.builder().chatId(who.toString())
				 			.replyToMessageId(mesage.getMessageId())
		                   .parseMode("HTML")
		                   .photo(inputFile)
//		                   .captionEntity(messageEntity)
		                   .replyMarkup(markup)
		                   .caption(text)
		                   .build();
		 
		 
		return photo;
		
	}
	@Override
	public SendMessage sendMessage(Long who, String what,Message mesage) {
//		 List<List<InlineKeyboardButton>> btns = getBtns();
//		 InlineKeyboardMarkup markup = InlineKeyboardMarkup.builder().keyboard(btns).build();
//		 StringBuffer buff = new StringBuffer();
////		 buff.append("<a href='https://www1.pconline.com.cn/pcedu/2021/art/images/banner_20210428.jpg'></a>");
////		 buff.append("<a href='https://www1.pconline.com.cn/pcedu/2021/art/images/banner_20210428.jpg'></a>");
////		 buff.append("\n\n");
////		 buff.append("<b>AAAAA</b>");
////		 buff.append("[​](https://www1.pconline.com.cn/pcedu/2021/art/images/banner_20210428.jpg)");
//		 buff.append("*bold text*");
//		 buff.append("\n");
//		 buff.append("_italic text_");
//		 SendMessage sm = SendMessage.builder().chatId(who.toString())
////				    .replyToMessageId(mesage.getMessageId())
//		            .parseMode("Markdown").text(buff.toString())
//		            .replyMarkup(markup)
////		            .disableWebPagePreview(false)
//		            .build();
//		 return sm;
		 return null;
		 
	}
}

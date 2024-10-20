package com.hml.command;


import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import com.hml.back.bean.IntegSeq;
import com.hml.back.bean.Order;
import com.hml.back.bean.Rank;
import com.hml.back.bean.RespBean;
import com.hml.back.service.BackCoreService;
import com.hml.bean.CommandTextParser;
import com.hml.config.BotConfig;
import com.hml.task.DrawInfo;
import com.hml.utils.FreeMarkerUtils;
import com.hml.utils.StringUtils;

@Service("bossCommand")
public class BossCommand extends BaseBtn {
	
	@Autowired
	private BackCoreService backCoreService;

	public SendMessage sendMessage(Long who,String what) {
		SendMessage sm = SendMessage.builder().chatId(who.toString())
	            .parseMode("Markdown")
	            .text(what)
	            .build();;
 		 return sm;
	}
	
	public SendPhoto sendPhoto(Long who, String what, Message mesage) {
		InputFile inputFile = getInputFile();
		String text = "<tg-emoji emoji-id=\"5368324170671202286\">🛎</tg-emoji>新的一期("+DrawInfo.DRAW_ISSUE+")开始，速来抢庄！ （最低"+what+"）";
//		 List<List<InlineKeyboardButton>> btns = getBtns();
//		 InlineKeyboardMarkup markup = InlineKeyboardMarkup.builder().keyboard(btns).build();
		 SendPhoto photo = SendPhoto.builder().chatId(who.toString())
		                   .parseMode("HTML")
		                   .photo(inputFile)
//		                   .replyMarkup(markup)
		                   .caption(text)
		                   .build();
		return photo;
	}
	public SendPhoto sendOverResult(Long who, RespBean bean) {
		String path = BotConfig.FILE_PATH + "/result_" + bean.getDataId() + ".jpg";
		Map<String, Object> map = getResultDataMap(bean);
		List<Map<String,Object>> list = (List<Map<String,Object>>)map.get("dataList");
		int height = 280;
		if(list != null && list.size() > 0) {
			int row = list.size() % 3 == 0 ? list.size() / 3 : list.size() / 3 + 1; 
			int maxHeight = ((row + 3 ) * 35) * 2;
			height = maxHeight < height ? height : maxHeight;
		}
		System.out.println("Result Height:" + height);
		File file = FreeMarkerUtils.turnImage("result.ftl", map,1400,height,"jpg",path);
		InputFile inputFile = new InputFile(file);
		
		SendPhoto photo = SendPhoto.builder().chatId(who.toString())
				.parseMode("Markdown")
				.photo(inputFile)
				.build();
		return photo;
	}
	public SendPhoto sendPhotoConfirm(Long who, RespBean bean) {
		String path = BotConfig.FILE_PATH + "/draw_" + bean.getDataId() + ".jpg";
		Map<String, Object> map = getRobDataMap(bean);
		File file = FreeMarkerUtils.turnImage("draw.ftl", map,960,410,"jpg",path);
		InputFile inputFile = new InputFile(file);
		
		String userName = bean.getUserName();
		String userId = bean.getUserId();
		BigDecimal money = bean.getIntegRal();
		StringBuffer buff = new StringBuffer();
		buff.append(String.format("恭喜%s为本场庄主！请玩家开始下注", userName)).append("\n");
		buff.append(String.format("当前庄主：%s",userName)).append("\n");
		buff.append(String.format("庄主金额：%d", money.toBigInteger())).append("\n");
		try {
			buff.append(String.format("近12期开奖：%s", backCoreService.getDraw())).append("\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
//		 List<List<InlineKeyboardButton>> btns = getBtns();
//		 InlineKeyboardMarkup markup = InlineKeyboardMarkup.builder().keyboard(btns).build();
		SendPhoto photo = SendPhoto.builder().chatId(who.toString())
				.parseMode("Markdown")
				.photo(inputFile)
//		                   .replyMarkup(markup)
				.caption(buff.toString())
				.build();
		return photo;
	}
	public SendPhoto sendPhotoConfirm(Long who, String userId, String userName,BigDecimal money) {
		InputFile inputFile = getInputFile();
		 
		StringBuffer buff = new StringBuffer();
		buff.append(String.format("恭喜%s为本场庄主！请玩家开始下注", userName)).append("\n");
		buff.append(String.format("当前庄主：%s",userName)).append("\n");
		buff.append(String.format("庄主金额：%d", money.toBigInteger())).append("\n");
//		 List<List<InlineKeyboardButton>> btns = getBtns();
//		 InlineKeyboardMarkup markup = InlineKeyboardMarkup.builder().keyboard(btns).build();
		 SendPhoto photo = SendPhoto.builder().chatId(who.toString())
		                   .parseMode("Markdown")
		                   .photo(inputFile)
//		                   .replyMarkup(markup)
		                   .caption(buff.toString())
		                   .build();
		return photo;
	}
	public SendPhoto sendOnlyPhotoResult(Long who,String what,int winNo) {
		InputFile inputFile = getInputFile(winNo);
		SendPhoto photo = SendPhoto.builder().chatId(who.toString())
                .parseMode("Markdown")
                .photo(inputFile)
                .caption(what)
                .build();
			return photo;
	}
	public SendPhoto sendPhotoResult(Long who, RespBean bean,boolean isBtn) {
		String path = BotConfig.FILE_PATH + "/" + bean.getDataId() + ".jpg";
		Map<String, Object> map = getDataMap(bean, "", "");
//		File file = ImageUtils.drawImg(map, path);
		int height = 600;
		if(bean.getOrderMx() != null && bean.getOrderMx().size() > 0) {
			int maxHeight = ((bean.getOrderMx().size() +3) * 36 + 40) * 2;
			height = maxHeight < height ? height : maxHeight;
		}
		System.out.println("Table Heihgt: " + height);
		File file = FreeMarkerUtils.turnImage("table.ftl", map,2160,height,"jpg",path);
		if(file  != null) {
			InputFile inputFile = new InputFile(file);
			if(isBtn) {
				List<List<InlineKeyboardButton>> btns = getBtns();
				 InlineKeyboardMarkup markup = InlineKeyboardMarkup.builder().keyboard(btns).build();
				 SendPhoto photo = SendPhoto.builder().chatId(who.toString())
				                   .parseMode("Markdown")
				                   .photo(inputFile)
				                   .replyMarkup(markup)
				                   .build();
				return photo;
			}else {
				 SendPhoto photo = SendPhoto.builder().chatId(who.toString())
				                   .parseMode("Markdown")
				                   .photo(inputFile)
				                   .build();
				return photo;
			}
		}else {
			return null;
		}
		 
	}
	public SendPhoto sendPhotoResultTable(Long who, Map<String, Object> map,String fileName,boolean isLast) {
		String path = BotConfig.FILE_PATH + "/" + fileName + ".jpg";
//		File file = ImageUtils.drawImg(map, path);
		File file = FreeMarkerUtils.turnImage("table.ftl", map,1100,500,"jpg",path);
		if(file  != null) {
			InputFile inputFile = new InputFile(file);
			SendPhoto photo = null;
			if(isLast) {
				List<List<InlineKeyboardButton>> btns = getBtns();
				 InlineKeyboardMarkup markup = InlineKeyboardMarkup.builder().keyboard(btns).build();
				 photo = SendPhoto.builder().chatId(who.toString())
				                   .parseMode("Markdown")
				                   .photo(inputFile)
				                   .replyMarkup(markup)
				                   .build();
			}else {
				 photo = SendPhoto.builder().chatId(who.toString())
				                   .parseMode("Markdown")
				                   .photo(inputFile)
				                   .build();
			}
			return photo;
		}else {
			return null;
		}
	}
	private Map<String,Object> getDataMap(RespBean bean,String title,String subTitle){
		Map<String,Object> dataMap = new HashMap<>();
		Map<String,Object> data = new HashMap<>();
		List<Map<String,Object>> dataList = new ArrayList<Map<String,Object>>();
		
		BigDecimal total = new BigDecimal(0);
		
		if(bean.getOrderMx() != null && bean.getOrderMx().size() > 0) {
			int index = bean.getOrderMx().size();
			for(Order order : bean.getOrderMx()) {
				Map<String,Object> item = new HashMap<String, Object>();
				item.put("userName",getSimpleName(order.getUserName()));
				item.put("fRate",order.getFRate().toString());
				item.put("integRal",String.valueOf(order.getIntegRal().longValue()));
				item.put("leftTRal",String.valueOf(order.getWinInteg().add(order.getIntegRal()).longValue()));
				item.put("winLose",order.getWinLose());
				item.put("winInteg",String.valueOf(order.getWinInteg().longValue()));
				item.put("RulId",order.getRulId());
				item.put("dBef",String.valueOf(order.getDBef().longValue()));
				item.put("dEnd",String.valueOf(order.getDEnd().longValue()));
				total = total.add(order.getIntegRal());
				item.put("seq",  String.valueOf(index--));
				dataList.add(item);
			}
			
			dataList.sort(new Comparator<Map<String,Object>>() {

				@Override
				public int compare(Map<String, Object> o1, Map<String, Object> o2) {
					String u1 = StringUtils.isBlank(o1.get("userName")) ? "" : o1.get("userName").toString();
					String u2 = StringUtils.isBlank(o2.get("userName")) ? "" : o2.get("userName").toString();
					return u1.compareTo(u2);
				}
				
			});
		}
		if(StringUtils.isBlank(bean.getUserName())) {
			data.put("count", dataList.size());
			data.put("total", String.valueOf(total.longValue()));
			data.put("drawNo", "");
			data.put("userName", "");
			data.put("winInteg", "");
			data.put("integRal", "");
			data.put("iLossNum", 0);
			data.put("iWinNum", 0);
		}else {
			data.put("count", dataList.size());
			data.put("total", String.valueOf(total.longValue()));
			data.put("drawNo", bean.getIWinNo() + CommandTextParser.getText(bean.getIWinNo()));
			data.put("userName", getSimpleName(bean.getUserName()));
			data.put("winInteg", bean.getWinInteg());
			data.put("integRal", String.valueOf(bean.getIntegRal().longValue()));
			data.put("iLossNum", bean.getILossNum());
			data.put("iWinNum", bean.getIWinNum());
		}
		
		
		dataMap.put("title", title);
		dataMap.put("subTitle", subTitle);
		dataMap.put("data", data);
		dataMap.put("dataList", dataList);
		
		return dataMap;
	}
	
	private Map<String,Object> getRobDataMap(RespBean res){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("userName", getSimpleName(res.getUserName()));
		map.put("integRal", String.valueOf(res.getIntegRal().longValue()));
		map.put("iCount", res.getICount());
		map.put("dBef", String.valueOf(res.getDBef().longValue()));
		map.put("min", DrawInfo.ORDER_MIN_MONEY);
		map.put("max", DrawInfo.ORDER_MAX_MONEY);
		Map<String,Object> dataMap = new HashMap<>();
		dataMap.put("data", map);
		return dataMap;
	}
	
	private Map<String, Object> getResultDataMap(RespBean res){
		Rank rank = res.getRankList();
		Map<String,Object> data = new HashMap<String, Object>();
		List<Map<String,Object>> dataList = new ArrayList<Map<String,Object>>();
		if(rank != null) {
			data.put("sumInteg", String.valueOf(rank.getSumInteg().longValue()));
			data.put("sCurTime", rank.getSCurTime());
			if(rank.getUserList() != null && rank.getUserList().size() > 0) {
				for(IntegSeq seq : rank.getUserList()) {
					Map<String,Object> item = new HashMap<String, Object>();
					item.put("dEnable", String.valueOf(seq.getDEnable().longValue()));
					item.put("userName", getSimpleName(seq.getUserName()));
					dataList.add(item);
				}
//				if(dataList.size() % 3 ==1) {
//					Map<String,Object> item = new HashMap<String, Object>();
//					item.put("dEnable", "");
//					item.put("userName", "");
//					dataList.add(item);
//					Map<String,Object> item2 = new HashMap<String, Object>();
//					item2.put("dEnable", "");
//					item2.put("userName", "");
//					dataList.add(item2);
//				}else if(dataList.size() % 3 ==2){
//					Map<String,Object> item = new HashMap<String, Object>();
//					item.put("dEnable", "");
//					item.put("userName", "");
//					dataList.add(item);
//				}
			}
		}else {
			data.put("sumInteg", "");
			data.put("sCurTime", "");
		}
		Map<String,Object> dataMap = new HashMap<>();
		dataMap.put("data", data);
		dataMap.put("dataList", dataList);
		return dataMap;
	}

	private String getSimpleName(String name) {
		if(name.length() > 3) {
			name = name.substring(0,3)+ "..";
		}
		return name;
	}
}

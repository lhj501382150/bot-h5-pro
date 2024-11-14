package com.hml.back.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import com.alibaba.fastjson.JSONObject;
import com.hml.back.bean.RespBean;
import com.hml.back.bean.RespMoney;
import com.hml.back.config.BackCoreConfig;
import com.hml.bean.CommandTextParser;
import com.hml.bean.DataSource;
import com.hml.bot.BaseBot;
import com.hml.command.BaseBtn;
import com.hml.redis.RedisKey;
import com.hml.redis.RedisUtils;
import com.hml.utils.HttpClientUtils;
import com.hml.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("backCoreService")
public class BackCoreService extends BaseBtn{
	
	@Autowired
	private RedisUtils redisUtils;
	
	
	public boolean isBotEnable(String chatId) {
		String key = RedisKey.ROB_STATUS + chatId;
		Object val = redisUtils.get(key);
		log.info("机器人状态：{}-{}",key,val);
		boolean flag = false;
		if(!StringUtils.isBlank(val)) {
			flag = "Y".equals(val.toString());
		}
		return flag;
	}
	
	public JSONObject isRob(JSONObject data) {
		
		return null;
	}
	public String getDraw() throws Exception{
		String result = "";
		try {
			JSONObject data = new JSONObject();
			String ret = HttpClientUtils.doPost(BackCoreConfig.GET_DRAW, data.toJSONString(),null);
			JSONObject json = JSONObject.parseObject(ret);
			if(!"0".equals(json.getString("iCode"))){
				throw new Exception(json.getString("sMsg"));
			}
			String str = json.getString("rData");
			if(!StringUtils.isBlank(str)) {
				String[] arr = str.split(",");
				StringBuffer buff = new StringBuffer();
				for(String no : arr) {
					buff.append(no).append(CommandTextParser.getText(Integer.parseInt(no)));
				}
				result = buff.toString();
			}
			
		} catch (Exception e) {
			throw e;
		}
		return result;
	}
	public String addGroupId(JSONObject data){
		String result = "ok";
		try {
			String ret = HttpClientUtils.doPost(BackCoreConfig.ADD_GROUPID, data.toJSONString(),null);
			JSONObject json = JSONObject.parseObject(ret);
			if(!"0".equals(json.getString("iCode"))){
				throw new Exception(json.getString("sMsg"));
			}
		} catch (Exception e) {
			result = e.getMessage();
		}
		return result;
	}
	
	public void addInviteAccount(BaseBot bot,Message msg) {
		String text = msg.getText();
		text = text.replace("/start", "").trim();
		Object val = redisUtils.hget(RedisKey.CHAT_INVITE_LINK, text);
		log.info("查询邀请链接：【{}】-{}",text,val);
		if(!StringUtils.isBlank(val)) {
			String link = val.toString();
			User user = msg.getFrom();
			String userName = getUserName(user);
			String userId = String.valueOf(user.getId());
			JSONObject data = new JSONObject();
			data.put("userId", userId);
			data.put("name", userName);
			if(StringUtils.isBlank(user.getUserName())) {
				data.put("userName", data.get("name"));
			}else {
				data.put("userName", user.getUserName());
			}
			data.put("openid", text);
			data.put("sex", "0");
			try {
				if(!userId.equals(text)) {
					String ret = HttpClientUtils.doPost(BackCoreConfig.ADD_ACCOUNT, data.toJSONString(),null);
					JSONObject json = JSONObject.parseObject(ret);
					if(!"0".equals(json.getString("iCode"))){
						throw new Exception(json.getString("sMsg"));
					}
				}
				SendMessage message = getSendMessage(msg.getChat().getId(), link, "HTML");
				bot.execute(message);
			} catch (Exception e) {
				link = "";
				log.error("添加邀请人异常：{}",e);
			}
			
		}
		
		
		
	}
	
	@Async
	public void addAccount(List<User> userList){
		for(User user: userList) {
			log.info("新入群成员：{}",user);
			String userName = getUserName(user);
			JSONObject data = new JSONObject();
			data.put("userId", String.valueOf(user.getId()));
			data.put("name", userName);
			if(StringUtils.isBlank(user.getUserName())) {
				data.put("userName", data.get("name"));
			}else {
				data.put("userName", user.getUserName());
			}
			data.put("openid", "-1");
			data.put("sex", "0");
			try {
				String ret = HttpClientUtils.doPost(BackCoreConfig.ADD_ACCOUNT, data.toJSONString(),null);
				JSONObject json = JSONObject.parseObject(ret);
				if(!"0".equals(json.getString("iCode"))){
					throw new Exception(json.getString("sMsg"));
				}
			} catch (Exception e) {
				log.error("添加账户异常:{}",e);
			}
		}
	}
	public JSONObject robOrder(JSONObject data) throws Exception{
		try {
			String ret = HttpClientUtils.doPost(BackCoreConfig.ROB_ORDER, data.toJSONString(),null);
			JSONObject json = JSONObject.parseObject(ret);
			if(!"0".equals(json.getString("iCode"))){
				throw new Exception(json.getString("sMsg"));
			}
			return json.getJSONObject("rData");
		} catch (Exception e) {
			throw e;
		}
	}
	
	public String downRob(JSONObject data){
		String result = "ok";
		try {
			String ret = HttpClientUtils.doPost(BackCoreConfig.DOWN_ROB, data.toJSONString(),null);
			JSONObject json = JSONObject.parseObject(ret);
			if(!"0".equals(json.getString("iCode"))){
				throw new Exception(json.getString("sMsg"));
			}
		} catch (Exception e) {
			result = e.getMessage();
		}
		return result;
	}
	
	public JSONObject order(JSONObject data) throws Exception{
		try {
			String ret = HttpClientUtils.doPost(BackCoreConfig.ORDER, data.toJSONString(),null);
			JSONObject json = JSONObject.parseObject(ret);
			if(!"0".equals(json.getString("iCode"))){
				throw new Exception(json.getString("sMsg"));
			}
			return json.getJSONObject("rData");
		} catch (Exception e) {
			log.error("下注异常： " + e.getMessage());
			throw e;
		} 
	}
	
	public RespBean queryOrder(JSONObject data){
		try {
			String ret = HttpClientUtils.doPost(BackCoreConfig.QUERY_ORDRE, data.toJSONString(),null);
			JSONObject json = JSONObject.parseObject(ret);
			if(!"0".equals(json.getString("iCode"))){
				throw new Exception(json.getString("sMsg"));
			}
			RespBean resp = JSONObject.parseObject(json.getString("rData"), RespBean.class);
			return resp;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public RespMoney queryMoney(User user){
		try {
			String userName = getUserName(user);
			JSONObject data = new JSONObject();
			data.put("userId", String.valueOf(user.getId()));
			data.put("name", userName);
			if(StringUtils.isBlank(user.getUserName())) {
				data.put("userName", data.get("name"));
			}else {
				data.put("userName", user.getUserName());
			}
			String ret = HttpClientUtils.doPost(BackCoreConfig.QUERY_MONEY, data.toJSONString(),null);
			JSONObject json = JSONObject.parseObject(ret);
			if(!"0".equals(json.getString("iCode"))){
				throw new Exception(json.getString("sMsg"));
			}
			RespMoney resp = JSONObject.parseObject(json.getString("rData"), RespMoney.class);
			return resp;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public RespMoney queryData(JSONObject data){
		try {
			String ret = HttpClientUtils.doPost(BackCoreConfig.QUERY_DATA, data.toJSONString(),null);
			JSONObject json = JSONObject.parseObject(ret);
			if(!"0".equals(json.getString("iCode"))){
				throw new Exception(json.getString("sMsg"));
			}
			RespMoney resp = JSONObject.parseObject(json.getString("rData"), RespMoney.class);
			return resp;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String addData(JSONObject data){
		String result = "ok";
		try {
			String ret = HttpClientUtils.doPost(BackCoreConfig.ADD_DATA, data.toJSONString(),null);
			JSONObject json = JSONObject.parseObject(ret);
			if(!"0".equals(json.getString("iCode"))){
				throw new Exception(json.getString("sMsg"));
			}
		} catch (Exception e) {
			result = e.getMessage();
		}
		return result;
	}
	
	public String addHqData(DataSource data){
		String result = "ok";
		try {
			JSONObject req = new JSONObject();
			req.put("sysTime", data.getSTime());
			req.put("hashData", "0x" + data.getHash());
			String ret = HttpClientUtils.doPost(BackCoreConfig.ADD_HQ, req.toJSONString(),null);
			JSONObject json = JSONObject.parseObject(ret);
			if(!"0".equals(json.getString("iCode"))){
				throw new Exception(json.getString("sMsg"));
			}
		} catch (Exception e) {
			result = e.getMessage();
		}
		return result;
	}
	private String getUserName(User user) {
		String first = StringUtils.isBlank(user.getFirstName()) ? "" : user.getFirstName();
		String last = StringUtils.isBlank(user.getLastName()) ? "" : user.getLastName();
		return first + last;
	}
}

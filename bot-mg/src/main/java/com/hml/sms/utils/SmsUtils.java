package com.hml.sms.utils;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hml.mall.entity.notice.Mess;
import com.hml.sms.bean.SendResponse;
import com.hml.sms.config.SmsProperties;
import com.hml.utils.DateTimeUtils;
import com.hml.utils.HttpClientUtils;
import com.hml.utils.StringUtils;

/*
 * 短信发送辅助类
 * 
 */
public class SmsUtils {
	private static Logger log = Logger.getLogger(SmsUtils.class);
	/*
	 * 发送短信
	 */
	public static Mess sendMess(Mess mess)throws Exception{
		log.info("发送短信：" + mess);
		try {
			String mobile = mess.getMobileno();
			if(StringUtils.isBlank(mobile)) {
				mobile = mess.getTelno();
				if(StringUtils.isBlank(mobile)){
					throw new Exception("手机号码不能为空");
				}
			}
			if(StringUtils.isBlank(mess.getTplid())) {
				throw new Exception("模板不能为空");
			}
			if(StringUtils.isBlank(mess.getContent())) {
				throw new Exception("短信内容不能为空");
			}
			String content = mess.getContent();
			String tplId = mess.getTplid();
			JSONObject params = createParam(mess.getMsgno(),tplId, content, mobile);
			String ret = SmsHttpUtls.doPost(SmsProperties.SEND_URL, params);
			SendResponse resp = parseResponse(ret);
			if(resp.getResultCode() == 0) {
				mess.setSendflag(1);
			}else {
				mess.setSendflag(2);
				mess.setMsg(resp.getResultDesc());
			}
		} catch (Exception e) {
			log.error("发送短信解析结果异常：" + e.getMessage());
			e.printStackTrace();
			mess.setSendflag(2);
			mess.setMsg(e.getMessage());
		}
		mess.setSendtime(DateTimeUtils.getDateTime());
		return mess;
	}

	/**
     * 构建国内短信的请求参数
     *
     * @param businessId 业务ID
     * @param templateId 短信模板ID。你需要先在易盾官网创建模板并通过审核后才能使用。模板ID需要与业务ID匹配。即，该模板属于目标业务。
     * @param variables  短信模板中的变量值。如，你的模板内容为 “您的验证码为${code}，有效期${time}分钟。”，则此参数需指明 code 和 time 的值。
     * @param to         收信方的号码。如，134开头的号码一般是中国移动的号码。
     */
    private static JSONObject createParam(String msgNo,String templateId, String content, String to) {
       JSONArray arr = new JSONArray();
       JSONObject item = new JSONObject();
       JSONArray mobiles = new JSONArray();
       mobiles.add(to);
       item.put("mobiles", mobiles);
       item.put("templateId", templateId);
       item.put("templateParas", JSONObject.parse(content));
       item.put("signature", SmsProperties.SIGNATURE);
       arr.add(item);
       
       JSONObject json = new JSONObject();
       json.put("account", SmsProperties.SECRET_ID);
       json.put("password", SmsProperties.SECRET_KEY);
       json.put("requestLists", arr);
       json.put("requestId", msgNo);
       json.put("statusCallback", "");

       return json;
    } 
	
     
    
    private static SendResponse parseResponse(String strResponse) throws Exception{
    	 return JSON.parseObject(strResponse, SendResponse.class);
    }
	
}

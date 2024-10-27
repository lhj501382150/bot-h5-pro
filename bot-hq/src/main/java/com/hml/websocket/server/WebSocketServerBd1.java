package com.hml.websocket.server;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.hml.bean.DataSource;
import com.hml.task.DrawInfo;
import com.hml.task.HqTaskManager;
import com.hml.utils.PasswordEncoder;

import lombok.extern.slf4j.Slf4j;

//这个是客户端访问的接口
@Slf4j
@ServerEndpoint("/socket/bd1/{userno}/{md5}")
@Component
public class WebSocketServerBd1 {
    /**静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。*/
    private static int onlineCount = 0;

    /**concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。*/
    private static ConcurrentHashMap<String,WebSocketServerBd1> webSocketMap = new ConcurrentHashMap<>();
    /**与某个客户端的连接会话，需要通过它来给客户端发送数据*/
    private Session session;
    private String userId="";
    public static Session temp;
    public static boolean flag=false;
    /**
     * 连接建立成功调用的方法*/
    @OnOpen
    public void onOpen(Session session, @PathParam("userno") String userno, @PathParam("md5") String md5) {
    	//获取到session和userId
        this.session = session;
        this.userId= userno;
        this.temp=session;
        flag=true;
        //如果userId存在，则移除，再添加一个相同的userId和新的Session（防止session过期）
        if(webSocketMap.containsKey(userId)){
            webSocketMap.remove(userId);
            webSocketMap.put(userId,this);
        }else{
            webSocketMap.put(userId,this);
            addOnlineCount();
        }

        log.info("用户{}连接,当前在线人数为:{}" ,userId, getOnlineCount());

        try {
        	DataSource item = HqTaskManager.getDraw("HXBD1");
        	JSONObject json = new JSONObject();
			json.put("ISSUE", item.getDrawIssue());
			json.put("CODE", item.getSResult());
			json.put("RESULT", item.getResult());
			json.put("TIME", item.getSTime());
			json.put("ID", item.getId());
            sendMessage(getMsg(1,json.toJSONString()));
        } catch (IOException e) {
            log.error("用户:{},网络异常!!!!!!",userId,e);
        }
 
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        //如果存在userId则移除，然后人数-1
        if(webSocketMap.containsKey(userId)){
            webSocketMap.remove(userId);
            //从set中删除
            subOnlineCount();
            flag=false;
        }
        log.info("用户{}退出,当前在线人数为:{}" ,userId, getOnlineCount());
    }

    /**
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("用户错误:{},原因:{}",this.userId,error.getMessage());
//        error.printStackTrace();
    }
    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 发送自定义消息
     * */
    public static void sendInfo(String message,@PathParam("userId") String userId) throws IOException {
        log.info("发送消息到:"+userId+"，报文:"+message);

        if(StringUtils.isNotBlank(userId)&&webSocketMap.containsKey(userId)){
            webSocketMap.get(userId).sendMessage(message);
        }else{
            log.error("用户"+userId+",不在线！");
        }
    }
    /**
     * 群发信息
     * @param message
     * @throws IOException
     */
    public static void sendInfo(int step,String message) throws IOException {
        log.info("发送消息报文:{}",message);
        String msg = getMsg(step,message);
        int i = 0;
        for(String key : webSocketMap.keySet()) {
        	webSocketMap.get(key).sendMessage(msg);
        	i++;
        }
        log.info("消息发送完成:【{}】",i);
    }
    public static String getMsg(String msg) {
    	return getMsg(0,msg);
    }
    public static String getMsg(int step,String msg) {
    	JSONObject json = new JSONObject();
    	json.put("code", 200);
    	json.put("data", msg);
    	json.put("status", step);
    	return json.toJSONString();
    }
    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServerBd1.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServerBd1.onlineCount--;
    }
    public void setSession(Session session) {
        this.session = session;
    }
    
    public boolean checkAuth(String userno) {
    	boolean flag  = false;
    	if(userno.contains("-")) {
    		String[] paras = userno.split("-");
    		if(paras.length==2) {
    			String text = paras[0] + paras[0];
        		text = new PasswordEncoder("").encode(text);
        		flag = text.equals(paras[1]);
    		}
    	}
    	return flag;
    }
}

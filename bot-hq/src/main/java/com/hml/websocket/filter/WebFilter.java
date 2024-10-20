package com.hml.websocket.filter;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.hml.redis.RedisKey;
import com.hml.redis.RedisUtils;
import com.hml.utils.IPUtils;
import com.hml.utils.PasswordEncoder;

import lombok.extern.slf4j.Slf4j;
 
@Slf4j
@javax.servlet.annotation.WebFilter(filterName = "sessionFilter", urlPatterns = "/*")
@Order(1)
@Component
public class WebFilter implements Filter {
	
	 private static final int CONNECTION_RATE_LIMIT = 10; // 每秒允许的最大连接数
	 private static final int CONNECTION_RATE_INTERVAL = 1000; // 时间间隔（毫秒）
	 private static final long EXPIRE_SECOND = 60 * 60 * 24; // 时间间隔（秒）
	 private static final String BLACK_IP = ",43.134.222.90,";
	 
	 private static final ConcurrentHashMap<String, Long> connectionTimes = new ConcurrentHashMap<>();
 
	 @Autowired
	 private RedisUtils redisUtils;
	 
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        String ipAddr = IPUtils.getIpAddr(req);
        Object ipObj = redisUtils.get(RedisKey.BLACK_IP_LIST);
        String blackIps = "";
        if(ipObj!= null) {
        	blackIps = ipObj.toString() + BLACK_IP;
        }else {
        	blackIps = BLACK_IP;
        }
        if(blackIps.indexOf(ipAddr) > -1) {
        	log.error("黑名单拦截:【{}】-{}-{}" ,ipAddr,req.getRequestURI(),blackIps);
        	return;
        }
        boolean flag = checkAuth(req,ipAddr);
        if(flag) {
        	if(checkUser(req.getRequestURI(),ipAddr)) {
        		filterChain.doFilter(servletRequest, servletResponse);
        	}else {
        		HttpServletResponse response = (HttpServletResponse)servletResponse;
           	 	response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        	}
        }else {
        	 HttpServletResponse response = (HttpServletResponse)servletResponse;
        	 response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
    
    public boolean checkAuth(HttpServletRequest req,String remoteAddress) {
    	boolean flag  = false;
    	long currentTime = System.currentTimeMillis();
    	String redisKey = RedisKey.BLACK_IP_REQUEST + remoteAddress;
    	Object obj = redisUtils.get(redisKey);
    	IPRecord ipData = null;
    	if(obj==null) {
    		ipData = new IPRecord();
    		ipData.setFirstTime(currentTime);
    	}else {
    		ipData = JSONObject.parseObject(obj.toString(),IPRecord.class);
    		if(ipData.getNum() >=  CONNECTION_RATE_LIMIT) {
    		   log.error("非法请求：{}-{}",remoteAddress,req.getRequestURI());
         	   return false;
            }
    	}
 
        Long lastConnectionTime = connectionTimes.get(remoteAddress);
        if (lastConnectionTime != null && currentTime - lastConnectionTime < CONNECTION_RATE_INTERVAL) {
        	log.error("异常IP：{}-【{}】-{}",remoteAddress,ipData.getNum(),req.getRequestURI());
        	ipData.setUri(req.getRequestURI());
        	ipData.setIpAddr(remoteAddress);
        	ipData.setNum(ipData.getNum() + 1);
        	ipData.setCurTime(currentTime);
        	ipData.setPreTime(lastConnectionTime);
        	redisUtils.set(redisKey, JSONObject.toJSONString(ipData),EXPIRE_SECOND);
        	flag = false;
        } else {
            connectionTimes.put(remoteAddress, currentTime);
            // 允许连接
            flag = true;
        }
        return flag;
    }
 
    public boolean checkUser(String uri,String ipAddr) {
    	boolean flag =false;
    	try {
    		String[] paras = uri.split("/");
    		if(paras.length >= 5) {
    			int length = paras.length;
    			String userno = paras[length-2];
    			String pwd = paras[length -1];
    			String text = new PasswordEncoder("").encode(userno+userno);
            	flag = text.equals(pwd);
            	log.info("用户【{}】-{}连接",userno,ipAddr);
    		}
		} catch (Exception e) {
			flag = false;
		}
    	return flag;
    }
}
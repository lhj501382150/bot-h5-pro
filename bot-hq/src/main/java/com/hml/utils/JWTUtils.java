package com.hml.utils;
 
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
 
 
public class JWTUtils {
 
    /**
     * 生成token  header.payload.singature
     */
    private static final String SING = "001";
 
 
    public static String getToken(Map<String, String> map) {
 
        Calendar instance = Calendar.getInstance();
        // 默认7天过期
        instance.add(Calendar.DATE, 7);
 
        //创建jwt builder
        JWTCreator.Builder builder = JWT.create();
 
        // payload
        map.forEach(builder::withClaim);
 
        return builder.withExpiresAt(instance.getTime())  //指定令牌过期时间
                .sign(Algorithm.HMAC256(SING));
    }
 
    /**
     * 验证token  合法性
     */
    public static DecodedJWT verify(String token) {
        return JWT.require(Algorithm.HMAC256(SING)).build().verify(token);
    }
    
    public static void main(String[] args) {
		String token  = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJBY2N0X05vIjoiMDAxIiwiV29ya2VyX05vIjoiMDAxIiwiUGFyZW50Tm8iOiIwMDEiLCJuYmYiOjE3MjE3MDI2OTQsImV4cCI6MTcyMTg3NTQ5NCwiaXNzIjoiV2ViQXBwSXNzdWVyIiwiYXVkIjoiV2ViQXBwQXVkaWVuY2UifQ.4WcwlQ0UkMIVS5bc8sz7MaSdtkIfQM93z9N85rAI4I8";
		try {
			Map<String,String> map = new HashMap<String,String>();
			map.put("ID", "001");
//			token = getToken(map);
			System.out.println(token);
			
			verify(token);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
}
 
package com.hml.sms.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SmsHttpUtls {

	private static Logger log = LoggerFactory.getLogger(SmsHttpUtls.class);
	
	public static String doPost(String url,Map<String,Object> body)throws Exception{
		log.info("请求路径：" + url);
		log.info("请求参数为：" + body);
		HttpsURLConnection connection = null; 
        InputStream is = null; 
        BufferedReader br = null; 
        trustAllHttpsCertificates(); 
        try { 
 
 
            URL realUrl = new URL(url); 
            connection = (HttpsURLConnection) realUrl.openConnection(); 
            connection.setHostnameVerifier(DO_NOT_VERIFY); 
            connection.setDoInput(true); // 设置可输入 
            connection.setDoOutput(true); // 设置该连接是可以输出的 
            connection.setRequestMethod("POST"); // 设置请求方式 
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8"); 
            // connection.connect(); 
            ObjectMapper objectMapper = new ObjectMapper(); 
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8")); 
            pw.write(objectMapper.writeValueAsString(body)); 
            pw.flush(); 
            pw.close(); 
 
 
            br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8")); 
            int status = connection.getResponseCode(); 
            if (200 == status) { // 200 
                is = connection.getInputStream(); 
            } else { // 400/401 
                is = connection.getErrorStream(); 
            } 
            br = new BufferedReader(new InputStreamReader(is, "UTF-8")); 
            String line = null; 
            StringBuilder result = new StringBuilder(); 
            while ((line = br.readLine()) != null) { // 读取数据 
                result.append(line + "\n"); 
            } 
            connection.disconnect(); 
            log.info("返回结果为：{}",result.toString() );
            return result.toString();
        } catch (Exception e) { 
            e.printStackTrace(); 
            throw e;
        } finally { 
            try { 
 
 
                if (null != is) { 
                    is.close(); 
                } 
                if (null != br) { 
                    br.close(); 
                } 
            } catch (Exception e) { 
                e.printStackTrace(); 
            } 
        } 
	}
	/** 
     * 设置不验证主机 
     */ 
	private static final HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() { 
        @Override 
        public boolean verify(String hostname, SSLSession session) { 
            return true; 
        } 
    }; 
    
    private static void trustAllHttpsCertificates() throws Exception { 
        TrustManager[] trustAllCerts = new TrustManager[] { 
            new X509TrustManager() { 
                @Override 
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException { 
                    return; 
                } 
                @Override 
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException { 
                    return; 
                } 
                @Override 
                public X509Certificate[] getAcceptedIssuers() { 
                    return null; 
                } 
            } 
        }; 
        SSLContext sc = SSLContext.getInstance("SSL"); 
        sc.init(null, trustAllCerts, null); 
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory()); 
    } 
}

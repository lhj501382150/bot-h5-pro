package com.hml.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class HttpClientUtils {
	 public static String doGet(String url,boolean isLog) throws Exception {
		 	if(isLog) {
		 		log.info("GET请求：" + url);
		 	}
	        try {
	            CloseableHttpClient httpclient = null;
		        if(url.startsWith("https:")){
		        	httpclient  = getHttpsClient();
		        }else{
		        	httpclient = getHttpClient();
		        }
	            //发送get请求
	            HttpGet request = new HttpGet(url);
	            HttpResponse response = httpclient.execute(request); 
	            
	            StatusLine status = response.getStatusLine();
	            int state = status.getStatusCode();
	            if (state == HttpStatus.SC_OK) {
	                HttpEntity responseEntity = response.getEntity();
	                String jsonString = EntityUtils.toString(responseEntity,"UTF-8");
	                if(isLog) {
	    		 		log.info("返回结果为:" + jsonString);
	    		 	}
	                return jsonString;
	            }
	            else{
	                log.error("请求返回:"+state+"("+url+")");
	                throw new Exception("请求异常：" + state);
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return null;
	    }

	    /**
	     * post请求(用于key-value格式的参数)
	     * @param url
	     * @param params
	     * @return
	     */
	    public static String doPost(String url, Map<String,Object> params){
	    	log.info("Post请求路径:" + url);
	    	log.info("Post请求参数:" + params);
	    	
	        BufferedReader in = null;
	        try {
	            // 定义HttpClient  
	            HttpClient client = getHttpClient();
	            // 实例化HTTP方法  
	            HttpPost request = new HttpPost();
	            request.setURI(new URI(url));

	            //设置参数
	            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
	            for (Iterator iter = params.keySet().iterator(); iter.hasNext();) {
	                String name = (String) iter.next();
	                String value = String.valueOf(params.get(name));
	                nvps.add(new BasicNameValuePair(name, value));

	                //System.out.println(name +"-"+value);
	            }
	            request.setEntity(new UrlEncodedFormEntity(nvps,HTTP.UTF_8));

	            HttpResponse response = client.execute(request);
	            int code = response.getStatusLine().getStatusCode();
	            if(code == 200){    //请求成功
	                in = new BufferedReader(new InputStreamReader(response.getEntity()
	                        .getContent(),"utf-8"));
	                StringBuffer sb = new StringBuffer("");
	                String line = "";
	                String NL = System.getProperty("line.separator");
	                while ((line = in.readLine()) != null) {
	                    sb.append(line + NL);
	                }

	                in.close();
	                log.info("返回结果为:" + sb.toString());
	                return sb.toString();
	            }
	            else{    //
	            	log.info("返回状态码为:" + code);
	                return null;
	            }
	        }catch(Exception e){
	        	log.error("请求异常:"+ e.getMessage());
	            e.printStackTrace();

	            return null;
	        }
	    }
	    /**
	     * post请求（用于请求json格式的参数）
	     * @param url
	     * @param params
	     * @return
	     */
	    public static String doPost(String url, String params) throws Exception {
	    	log.info("Post请求路径:" + url);
	    	log.info("Post请求参数:" + params);
	        CloseableHttpClient httpclient = null;
	        if(url.startsWith("https:")){
	        	httpclient  = getHttpsClient();
	        }else{
	        	httpclient = getHttpClient();
	        }
	        HttpPost httpPost = new HttpPost(url);// 创建httpPost   
	        httpPost.setHeader("Accept", "application/json");
	        httpPost.setHeader("Content-Type", "application/json");
	        String charSet = "UTF-8";
	        StringEntity entity = new StringEntity(params, charSet);
	        httpPost.setEntity(entity);
	        CloseableHttpResponse response = null;

	        try {

	            response = httpclient.execute(httpPost);
	            StatusLine status = response.getStatusLine();
	            int state = status.getStatusCode();
	            if (state == HttpStatus.SC_OK) {
	                HttpEntity responseEntity = response.getEntity();
	                String jsonString = EntityUtils.toString(responseEntity,"UTF-8");
	                log.info("返回结果为:" + jsonString);
					if(jsonString.startsWith("\"")) {
						int beg = jsonString.indexOf("{");
						int end = jsonString.indexOf("}");
						jsonString = jsonString.substring(beg,end+1);
					}
	                jsonString = jsonString.replaceAll("\\\\", "");
	                return jsonString;
	            }
	            else{
	                log.error("请求返回:"+state+"("+url+")");
	                throw new Exception("请求异常：" + state);
	            }
	        }
	        finally {
	            if (response != null) {
	                try {
	                    response.close();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	            try {
	                httpclient.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	    
	private static CloseableHttpClient getHttpsClient()throws Exception{
		try {
			SSLContext ctx = SSLContext.getInstance("SSL");
			X509TrustManager manager = new X509TrustManager() {
				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
				@Override
				public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}
				@Override
				public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}
			};
			ctx.init(null, new TrustManager[]{manager}, null);
			SSLConnectionSocketFactory factory = new SSLConnectionSocketFactory(ctx,NoopHostnameVerifier.INSTANCE);
			
			RequestConfig config = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT)
						.setExpectContinueEnabled(Boolean.TRUE)
						.setTargetPreferredAuthSchemes(Arrays.asList("NTLM","Digest"))
						.setSocketTimeout(5000)
					    .setConnectTimeout(5000)
					    .setConnectionRequestTimeout(5000)
						.setProxyPreferredAuthSchemes(Arrays.asList("Basic")).build();
			
			Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
			.register("http", PlainConnectionSocketFactory.INSTANCE)
			.register("https", factory).build();
			
			PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
			CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connectionManager)
						.setDefaultRequestConfig(config).build();
			//CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(factory).build();
			return httpClient;
		} catch (Exception e) {
			e.printStackTrace();
			return  HttpClients.createDefault();
		}
	}
	 
	private static CloseableHttpClient getHttpClient(){
		RequestConfig config = RequestConfig.custom()
			    .setSocketTimeout(5000)
			    .setConnectTimeout(5000)
			    .setConnectionRequestTimeout(5000)
			    .setStaleConnectionCheckEnabled(true)
			    .build();
		CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(config).build();
		return httpClient;
	}
}

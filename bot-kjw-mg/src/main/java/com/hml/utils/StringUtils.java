package com.hml.utils;

import com.alibaba.fastjson.JSONArray;

/**
 * 字符串工具类
 * @author hml
 * @date Sep 1, 2018
 */
public class StringUtils {

	/**
	 * 判空操作
	 * @param value
	 * @return
	 */
	public static boolean isBlank(String value) {
		return value == null || "".equals(value) || "null".equals(value) || "undefined".equals(value);
	}
	
	/**
	 * 判空操作
	 * @param value
	 * @return
	 */
	public static boolean isBlank(Object obj) {
		String value = "";
		if(obj==null) {
			value="";
		}else {
			value= obj.toString();
		}
		return isBlank(value);
	}
	
	 public static String trimToNull(String str) {
        String ts = trim(str);
        return isBlank(ts) ? null : ts;
    }
	 
	 public static String trim(String str) {
        return str == null ? null : str.trim();
    }

	 public static String getStr(JSONArray arr) {
		StringBuffer buff = new StringBuffer();
		for(int i = 0;i<arr.size();i++) {
			if(i == arr.size() - 1) {
				buff.append(arr.get(i));
			}else {
				buff.append(arr.get(i)).append(",");
			}
		}
		return buff.toString();
	}
	 
}

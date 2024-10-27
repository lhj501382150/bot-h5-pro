package com.hml.utils;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class RandomStringGenerator {

    private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
    private static final String NUMBER = "0123456789"; // 以保证1-9之间数字至少出现一次
    private static final String ALL_CHAR = CHAR_LOWER + CHAR_UPPER + NUMBER;
    private static final SecureRandom random = new SecureRandom();

    public static String replaceNum(String str,String nums) {
    	nums = nums.replace("10", "A");
    	nums = nums.replaceAll("0", "");
    	nums = nums.replace("A", "0");
    	String result = getResult(str);
    	result = result.replace("10", "A");
    	result = result.replaceAll("0", "");
    	result = result.replace("A", "0");
    	String[] numss = nums.split(",");
    	String[] results = result.split(",");
    	 
    	for(int i=0;i < results.length;i++) {
    		str = str.replaceAll(results[i], results[i]+"_A");
    	}
    	for(int i=0;i < results.length;i++) {
    		str = str.replaceAll(results[i]+"_A", numss[i]);
    	}
    	return str;
    	
    }
    public static String getHashStr() {
        StringBuilder sb = new StringBuilder(64);

        // 首先确保1-9之间的数字至少出现一次
        for (int i = 0; i < NUMBER.length(); i++) {
            sb.append(NUMBER.charAt(i));
        }
        // 填充剩余的部分
        for (int i = 10; i < 64; i++) {
            int index = random.nextInt(ALL_CHAR.length());
            sb.append(ALL_CHAR.charAt(index));
        }

        // 将字符串打乱以确保数字不是按顺序排列
        char[] chars = sb.toString().toCharArray();
        for (int i = 0; i < chars.length; i++) {
            int randomIndex = random.nextInt(chars.length);
            char temp = chars[i];
            chars[i] = chars[randomIndex];
            chars[randomIndex] = temp;
        }

        return new String(chars);
    }
    
    public static String  getResult(String str) {
        List<Character> uniqueDigits = new ArrayList<>();
        for (char c : str.toCharArray()) {
            if (Character.isDigit(c) && !uniqueDigits.contains(c)) {
                uniqueDigits.add(c);
            }
        }
        
        String uniqueDigitsStr = uniqueDigitsToString(uniqueDigits);
        uniqueDigitsStr = uniqueDigitsStr.replace("00", "10");
        return uniqueDigitsStr;
    }
    public static String uniqueDigitsToString(List<Character> uniqueDigits) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < uniqueDigits.size(); i++) {
        	String num = "0" + String.valueOf(uniqueDigits.get(i));
        	 
            sb.append(num);
            if (i < uniqueDigits.size() - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
		String hashStr = getHashStr();
		System.out.println(hashStr);
		String result = getResult(hashStr);
		
		System.out.println(result);
		result = "07,10,06,08,09,04,03,05,01,02";
		String str = replaceNum(hashStr, result);
		System.out.println(str);
		result = getResult(str);
		System.out.println(result);
	}
     
}
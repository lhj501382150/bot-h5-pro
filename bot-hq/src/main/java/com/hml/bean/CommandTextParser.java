package com.hml.bean;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.codec.binary.Base64;

public class CommandTextParser {
	 
//	public static void main(String[] args) {
//		while(true) {
//			System.out.println("Enter in:");
//			Scanner scanner = new Scanner(System.in);
//			String nextLine = scanner.nextLine();
//			boolean result = checkCommand(nextLine);
//			System.out.println(result);
//		}
//	}
	
	public static String textToNum(String text) {
		int length = text.length();
		String lastText = text.substring(length - 1);
		String result = "";
		char[] arr = text.toCharArray();
		if(lastText.equals(ZL.JIAO.getText()) && length == 3) {
			String s1 = Character.toString(arr[0]);
			String s2 = Character.toString(arr[1]);
			int a1 = getNum(s1);
			int a2 = getNum(s2);
			result = String.valueOf(a1) + String.valueOf(a2);
		}else if(lastText.equals(ZL.CHUAN.getText()) && length == 3) {
			String s1 = Character.toString(arr[0]);
			String s2 = Character.toString(arr[1]);
		    int a1 = getNum(s1);
		    int a2 = getNum(s2);
		    result = String.valueOf(a1) + String.valueOf(a2);
		}else if(lastText.equals(ZL.GU.getText()) && length == 2) {
			String s1 = Character.toString(arr[0]);
			int a1 = getNum(s1);
			result = String.valueOf(a1);
//		}else if(lastText.equals(ZL.MING.getText()) && length == 2) {
//			result = check2(arr);
		}else if(lastText.equals(ZL.TONG.getText()) && length == 2) {
			String s1 = Character.toString(arr[0]);
			int a = getNum(s1);
			int a2 = a + 2 > 4 ? a -2 : a + 2;
			
			result = String.valueOf(a) + String.valueOf(a2);
		}else if(lastText.equals(ZL.WEI.getText()) && length == 4) {
			String s1 = Character.toString(arr[0]);
			String s2 = Character.toString(arr[2]);
		    int a1 = getNum(s1);
		    int a2 = getNum(s2);
		    result = String.valueOf(a1) + String.valueOf(a2);
		}else if(text.indexOf(ZL.ZHENG.getText()) > -1 && length == 4) {
			String s1 = Character.toString(arr[0]);
			String s2 = Character.toString(arr[3]);
		    int a1 = getNum(s1);
		    int a2 = getNum(s2);
		    result = String.valueOf(a1) + String.valueOf(a2);
		}
		return result;
	}
	
	public static boolean checkCommand(String text) {
		int length = text.length();
		String lastText = text.substring(length - 1);
		boolean result = false;
		char[] arr = text.toCharArray();
		if(lastText.equals(ZL.JIAO.getText()) && length == 3) {
			result = check3jiao(arr);
		}else if(lastText.equals(ZL.CHUAN.getText()) && length == 3) {
			result = check3chuan(arr);
		}else if(lastText.equals(ZL.GU.getText()) && length == 2) {
			result = check2(arr);
//		}else if(lastText.equals(ZL.MING.getText()) && length == 2) {
//			result = check2(arr);
		}else if(lastText.equals(ZL.TONG.getText()) && length == 2) {
			result = check2(arr);
		}else if(lastText.equals(ZL.WEI.getText()) && length == 4) {
			result = check4wei(arr);
		}else if(text.indexOf(ZL.ZHENG.getText()) > -1 && length == 4) {
			result = check4nian(arr);
		}
		return result;
	}
	
	private static boolean check2(char[] arr) {
		boolean result = "入龙出虎".indexOf(Character.toString(arr[0])) > -1;
		return result;
	}
	
	private static boolean check3jiao(char[] arr) {
		String s1 = Character.toString(arr[0]);
		String s2 = Character.toString(arr[1]);
	    int a1 = getNum(s1);
	    int a2 = getNum(s2);
		return getNear(a1, a2);
	} 
	private static boolean check3chuan(char[] arr) {
		String s1 = Character.toString(arr[0]);
		String s2 = Character.toString(arr[1]);
	    int a1 = getNum(s1);
	    int a2 = getNum(s2);
		return getOpp(a1, a2);
	}
	private static boolean check4nian(char[] arr) {
		String s1 = Character.toString(arr[0]);
		String s2 = Character.toString(arr[3]);
	    int a1 = getNum(s1);
	    int a2 = getNum(s2);
		return getNear(a1, a2);
	}
	private static boolean check4wei(char[] arr) {
		String s1 = Character.toString(arr[0]);
		String s2 = Character.toString(arr[2]);
	    int a1 = getNum(s1);
	    int a2 = getNum(s2);
		return getOpp(a1, a2);
	}
	private static boolean getNear(int a,int b) {
		int min = a - 1 < 1 ? 4 : a - 1 ;
		int max = a + 1 > 4 ? 1 : a + 1;
		if(b == min || b == max) {
			return true;
		}
		return false;
	}
	
	private static boolean getOpp(int a,int b) {
		int opp = a + 2 > 4 ? a -2 : a + 2;
		return b == opp;
	}
	
	public static String getText(int num) {
		if(num == 1) {
			return RLCH.RU.text;
		}else if(num ==2) {
			return RLCH.LONG.text;
		}else if(num == 3) {
			return RLCH.CHU.text;
		}else if(num ==4) {
			return RLCH.HU.text;
		}
		return "";
	}
	private static int getNum(String text) {
		if(text.equals(RLCH.RU.text)) {
			return 1;
		}else if(text.equals(RLCH.LONG.text)) {
			return 2;
		}else if(text.equals(RLCH.CHU.text)) {
			return 3;
		}else if(text.equals(RLCH.HU.text)) {
			return 4;
		}
		return 0;
	}
	
	public static void main(String[] args) {
		String path = "E:\\workspace2\\Telegram-bot\\src\\main\\resources\\images\\table.jpg";
		 try (FileInputStream fis = new FileInputStream(path)) {
	            byte[] bytes = new byte[fis.available()];
	            fis.read(bytes);

	            Base64 base64 = new Base64();
	            String str =  base64.encodeToString(bytes);
	            System.out.println(str);
	        } catch (IOException e) {
	            e.printStackTrace();
	            
	        }
	}
}

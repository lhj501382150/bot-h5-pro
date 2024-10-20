package com.hml.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.springframework.core.io.ClassPathResource;

public class ImageUtils {
	 
	@SuppressWarnings("resource")
	public static File drawImg(Map<String,Object> data,String outImagePath) {
	    InputStream inputStream = null;
	    OutputStream outputStream = null;
	    try {
	    	inputStream = new ClassPathResource("images/table.jpg").getInputStream();
	        Image src = ImageIO.read(inputStream);
	        // 获取图片的高和宽
	        int wideth = src.getWidth(null);
	        int height = src.getHeight(null);
	        // 新增一个图片缓冲
	        BufferedImage image = new BufferedImage(wideth, height, BufferedImage.TYPE_INT_RGB);
	        Graphics g = image.createGraphics();
	        g.drawImage(src, 0, 0, wideth, height, null);
	        // 设置字体颜色（颜色也可以直接new定义rgba，例如new Color(17, 16, 44)）
	        g.setColor(Color.BLACK);
	        // size字体大小，Font.BOLD字体加粗
	        g.setFont(new Font("宋体", Font.BOLD, 20));
	        // 写入第一行
	        Object head = data.get("data");
	        if(head != null) {
	        	Map<String,Object> headMap = (Map)head;
	        	writeHead(g,headMap , wideth, height);
	        	List<Map<String,Object>> dataList = (List<Map<String,Object>>)data.get("dataList");
	        	if(dataList != null && dataList.size() > 0) {
	        		writeData(g, dataList, wideth, height);
	        	}
	        }
	        // 释放资源
	        g.dispose();
	        File file = new File(outImagePath);
            //关闭流
	        outputStream = new FileOutputStream(outImagePath);
	        ImageIO.write(image, "png", file);
	        return file;
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    } finally {
	        try {
				if(inputStream != null) {
					inputStream.close();
				}
				if(outputStream != null) {
					outImagePath.getClass();
				}
			} catch (Exception e2) {
				// TODO: handle exception
			}
	    }
	}
	
	private static void writeHead(Graphics g,Map<String,Object> map,int width,int height)throws Exception{
		g.drawString(getVal(map, "count"), width - 1050, height - 472);
		g.drawString(getVal(map, "total"), width - 925, height - 472);
		g.drawString(getVal(map, "drawNo"), width - 795, height - 472);
		g.drawString(getVal(map, "userName"), width - 690, height - 472);
		g.drawString(getVal(map, "winInteg"), width - 520, height - 472);
		g.drawString(getVal(map, "integRal"), width - 411, height - 472);
		g.drawString("吃"+ getVal(map, "iWinNum") + "赔"+getVal(map, "iLossNum"), width - 290, height - 472);
	}
	private static void writeData(Graphics g,List<Map<String,Object>> datas,int width,int height)throws Exception{
		int rowHeight = 33;
		int baseHeight = 402;
		int row = 0;
		for(Map<String,Object> map : datas) {
			int tempHeight = height - baseHeight + (rowHeight * row);
			g.drawString(getVal(map, "seq"), width - 1050, tempHeight);
			g.drawString(getVal(map, "userName"), width - 950, tempHeight);
			g.drawString(getVal(map, "RulId"), width - 760, tempHeight);
			g.drawString(getVal(map, "integRal"), width - 630, tempHeight);
			g.drawString(getVal(map, "fRate"), width - 480, tempHeight);
			String winInteg = getVal(map, "winInteg");
			if(winInteg.indexOf("-") > -1) {
				g.setColor(Color.RED);
			}
			g.drawString(winInteg, width - 288, tempHeight);
			g.setColor(Color.BLACK);
			row++;
		}
	}
	
	private static String getVal(Map<String,Object> map,String key) {
		return getVal(map,key,"");
		
	}
	private static String getVal(Map<String,Object> map,String key,String defaultVal) {
		String val = "";
		if(!StringUtils.isBlank(map.get(key))) {
			val = map.get(key).toString();
		}
		return val;
		
	}

}

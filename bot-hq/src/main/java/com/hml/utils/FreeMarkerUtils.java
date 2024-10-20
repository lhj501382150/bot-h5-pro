package com.hml.utils;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.swing.Java2DRenderer;
import org.xml.sax.SAXException;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FreeMarkerUtils {
   


    /**
     * FreemarkerUtils加载index.ftl模板，渲染数据
     * @param template
     * @param map
     * @return
     * @throws IOException
     * @throws TemplateException
     */
    public static String getTemplate(String template, Map<String, Object> map) {
   
        //创建freeMarker配置对象
        Configuration cfg = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        //设置读取模板的基础路径
        cfg.setTemplateLoader(new ClassTemplateLoader(
        FreeMarkerUtils.class.getClass().getClassLoader(), "/templates"));
        //设置编码格式
        cfg.setDefaultEncoding(StandardCharsets.UTF_8.toString());
        //设置模板异常处理
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        //选择日志处理
        cfg.setLogTemplateExceptions(true);
        Template temp = null;
        String result = "";
        try {
   
            //获取模板问价
            temp = cfg.getTemplate(template, StandardCharsets.UTF_8.toString());
            StringWriter stringWriter = new StringWriter();
            //替换占位符
            temp.process(map, stringWriter);
            stringWriter.flush();
            stringWriter.close();
            //获取ftl内容字符串
            result = stringWriter.getBuffer().toString();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
   
            log.info("模板读取异常" + e.getBlamedExpressionString());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 生成Image
     * @param template 模板地址
     * @param map 替换对象
     * @param width 图片宽度
     * @param height 图片高度
     * @throws Exception
     */
    public static File turnImage(String template, Map<String, Object> map,Integer width,Integer height,String format,String outImagePath) {
   
        //获取freeMarker替换占位符后的字符串
        String html = getTemplate(template, map);
        //将字符串转为bytes
        byte[] bytes = html.getBytes(StandardCharsets.UTF_8);
        //将byte[]转换为流
        ByteArrayInputStream bin = new ByteArrayInputStream(bytes);
        //实例化文档工厂
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
   
            //利用文档工厂实例化文档
            DocumentBuilder builder = factory.newDocumentBuilder();
            //解析流
            Document document = builder.parse(bin);
            //设置渲染图片的像素（宽和高）
            Java2DRenderer renderer = new Java2DRenderer(document, width, height);
            SharedContext sharedContext = renderer.getSharedContext();
            sharedContext.setDotsPerPixel(2);
            sharedContext.setDPI(300);
            //获取渲染后的BufferedImage
            BufferedImage img = renderer.getImage();
            //在生成图片
            File file = new File(outImagePath);
            ImageIO.write(img,format,file);
            //关闭流
            bin.close();
            return file;
        } catch (ParserConfigurationException e) {
            log.info("解析流出现异常！" + e.getStackTrace());
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void main(String[] args) {
    	 
        Map<String, Object> map = getResultData();
//        Map<String, Object> map = getDataMap();
        List<Map<String,Object>> list = (List<Map<String,Object>>)map.get("dataList");
        int height = (list.size() / 3 + 3) * 37 * 2;
        System.out.println(height);
//        	turnImage("table.ftl", map,1100,height ,"jpg","F:\\data\\test.jpg");
        turnImage("result.ftl", map,1400,height,"jpg","F:\\data\\test.jpg");
//        html2Image("result.ftl", map,"jpg","F:\\data\\test.jpg");
//        ImageUtils.drawImg(map, "F:\\data\\test.jpg");
    }
     
    
    private static Map<String,Object> getDataMap(){
		Map<String,Object> dataMap = new HashMap<>();
		Map<String,Object> data = new HashMap<>();
		List<Map<String,Object>> dataList = new ArrayList<Map<String,Object>>();
		
		BigDecimal total = new BigDecimal(0);
		Map<String,Object> item = new HashMap<String, Object>();
		item.put("userName","eryueheny");
		item.put("fRate","3.0");
		item.put("integRal","1203");
		item.put("leftTRal","1203");
		item.put("winLose",100);
		item.put("winInteg",20);
		item.put("RulId","30");
		item.put("dBef","3");
		item.put("dEnd","5");
		total = total.add(new BigDecimal(100));
		item.put("seq",  "1");
		dataList.add(item);
		dataList.add(item);
		dataList.add(item);
		dataList.add(item);
		dataList.add(item);
		dataList.add(item);
		dataList.add(item);
		dataList.add(item);
		data.put("count", dataList.size());
		data.put("total", total);
		data.put("drawNo", "");
		data.put("userName", "");
		data.put("winInteg", "");
		data.put("integRal", "");
		data.put("iLossNum", 0);
		data.put("iWinNum", 0);
		
		
		dataMap.put("title", "aa");
		dataMap.put("subTitle", "123");
		dataMap.put("data", data);
		dataMap.put("dataList", dataList);
		
		return dataMap;
	}
    
    private static Map<String,Object> getResultData(){
    	Map<String,Object> dataMap = new HashMap<>();
		Map<String,Object> data = new HashMap<>();
		List<Map<String,Object>> dataList = new ArrayList<Map<String,Object>>();
		
		BigDecimal total = new BigDecimal(0);
		Map<String,Object> item = new HashMap<String, Object>();
		item.put("userName","eryueheny");
		item.put("dEnable",100);
		 
		total = total.add(new BigDecimal(100));
		dataList.add(item);
		dataList.add(item);
		dataList.add(item);
		dataList.add(item);
		dataList.add(item);
		dataList.add(item);
		dataList.add(item);
		dataList.add(item);
		dataList.add(item);
		dataList.add(item);
		dataList.add(item);
		dataList.add(item);
		dataList.add(item);
		data.put("sumInteg", total);
		data.put("sCurTime", "");
		 
		dataMap.put("data", data);
		dataMap.put("dataList", dataList);
		
		return dataMap;
    }

}
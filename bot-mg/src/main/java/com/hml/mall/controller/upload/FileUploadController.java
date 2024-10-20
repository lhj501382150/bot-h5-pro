package com.hml.mall.controller.upload;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@RestController
public class FileUploadController {


    //使用默认路径
    @RequestMapping("/upload")
    public Map upload(HttpServletRequest request) throws Exception {
//        fileUploadService.upload(file, null);
    	System.out.println(request);
    	MultipartHttpServletRequest req = (MultipartHttpServletRequest)request;
    	System.out.println(req.getFileMap());
    	//System.out.println(file);
    	System.out.println("------------");
    	Map<String,Object> map = new HashMap<>();
    	map.put("error", 0);
    	map.put("url", "http://192.168.208.129:8001/api/captcha.jpg");
        return map;
    }

    //自定义路径
    @RequestMapping("/upload/template")
    public String uploadPlace(MultipartFile file) throws Exception {
//        fileUploadService.upload(file, "H:\\upload");

        return null;
    }

    //下载
    @GetMapping("/download/file")
    public String downloadFile(HttpServletResponse response) throws IOException {
//        fileUploadService.download(response, "上传模板");

        return null;
    }
}
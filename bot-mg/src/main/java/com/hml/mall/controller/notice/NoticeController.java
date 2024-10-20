package com.hml.mall.controller.notice;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.hml.core.http.HttpResult;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.notice.Notice;
import com.hml.mall.iface.notice.INoticeService;
import com.hml.utils.HttpClientUtils;
import com.hml.utils.StringUtils;


/**
*  前端控制器
* @author hml
* @since 2021-04-20
*/


@RestController
@RequestMapping("/notice" )
public class NoticeController {
	private Logger log = Logger.getLogger(NoticeController.class);
    @Autowired
    private INoticeService  noticeService;

    @Value("${fileService.path}")
    private String fileServicePath = "";
    /**
    * 保存
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('info:notice:add')")
    @RequestMapping("/save")
    public HttpResult save(@RequestBody Notice model) {
    	
//    	if(!StringUtils.isBlank(model.getContent())){
//    		log.info("上传公告信息:"+fileServicePath);
//    		Map<String, Object> paraMap = new HashMap<>();	
//    		paraMap.put("type", "html");
//    		String content = fillContent(model.getContent(), model.getTitle());
//    		paraMap.put("content", content);
//    		String str = HttpClientUtils.doPost(fileServicePath+"/uploadContent", paraMap);
//    		JSONObject ret = JSONObject.parseObject(str);
//    		if("0".equals(ret.get("error").toString())){
//    			String url = ret.get("url").toString();
//    			model.setPath(url);
//    		}else{
//    			return HttpResult.error(ret.get("msg").toString());
//    		}
//    	}
        noticeService.save(model);
        return HttpResult.ok();
    }
 	/**
    * 修改
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('info:notice:edit')")
    @RequestMapping("/edit")
    public HttpResult edit(@RequestBody Notice model) {
//    	if(!StringUtils.isBlank(model.getContent())){
//    		log.info("上传公告信息:"+fileServicePath);
//    		Map<String, Object> paraMap = new HashMap<>();	
//    		paraMap.put("type", "html");
//    		String content = fillContent(model.getContent(), model.getTitle());
//    		paraMap.put("content", content);
//    		paraMap.put("path", model.getPath());
//    		String str = HttpClientUtils.doPost(fileServicePath+"/uploadContent", paraMap);
//    		JSONObject ret = JSONObject.parseObject(str);
//    		if("0".equals(ret.get("error").toString())){
//    			String url = ret.get("url").toString();
//    			model.setPath(url);
//    		}else{
//    			return HttpResult.error(ret.get("msg").toString());
//    		}
//    	}
        noticeService.updateById(model);
        return HttpResult.ok();
    }
	/**
    * 删除
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('info:notice:del')")
    @RequestMapping("/delete")
    public HttpResult delete(@RequestBody Notice model) {
        noticeService.removeById(model);
        return HttpResult.ok();
    }
    /**
    * 查询详情
    *
    * @param id
    * @return
    */
    @PreAuthorize("hasAuthority('info:notice:view')")
    @RequestMapping("/getContent")
    public HttpResult getContent(@RequestBody Notice model) {
//    	String path = model.getPath();
//    	String content = "";
//    	if(!StringUtils.isBlank(path)){
//    		Map<String,Object> paraMap = new HashMap<>();
//    		paraMap.put("path", path);
//    		content = HttpClientUtils.doPost(fileServicePath + "/downloadFile", paraMap);
//    	}
        return HttpResult.ok(model.getContent());
    }


    /**
    * 列表查询（非分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('info:notice:view')")
    @RequestMapping("/list")
    public HttpResult list() {
        Notice model = new  Notice();
        List<Notice> list =  noticeService.list(model);
        // todo 再包装一层
        return HttpResult.ok(list);
    }


    /**
    * 列表查询（分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('info:notice:view')")
    @RequestMapping("/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {

        PageResult page = noticeService.findPage(pageRequest);
        // todo 再包装一层
        return HttpResult.ok(page);
    }

    private String fillContent(String content,String title){
    	StringBuffer buff = new StringBuffer();
    	buff.append(" <!DOCTYPE html>                ");
    	buff.append(" <html lang=\"zh-CN\">            ");
    	buff.append(" <head>                         ");
    	buff.append("     <meta charset=\"UTF-8\">     ");
    	buff.append("     <title>       ");
    	buff.append(title);
    	buff.append("     </title>       ");
    	buff.append(" </head>                        ");
    	buff.append(" <body>                         ");
        buff.append(content);
    	buff.append(" </body>                        ");
    	buff.append(" </html>                        ");
    	return buff.toString();
    }
}

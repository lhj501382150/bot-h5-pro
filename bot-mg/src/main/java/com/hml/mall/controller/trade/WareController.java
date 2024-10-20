package com.hml.mall.controller.trade;


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
import com.hml.mall.entity.trade.Basepara;
import com.hml.mall.entity.trade.Ware;
import com.hml.mall.entity.trade.WareAttr;
import com.hml.mall.iface.trade.IWareService;
import com.hml.utils.HttpClientUtils;
import com.hml.utils.StringUtils;


/**
*  前端控制器
* @author hml
* @since 2021-04-17
*/


@RestController
@RequestMapping("/ware" )
public class WareController {
	private Logger log = Logger.getLogger(WareController.class);
    @Autowired
    private IWareService  wareService;
    
    @Value("${fileService.path}")
    private String fileServicePath = "";

    /**
    * 保存
    *
    * @param model
    * @return
     * @throws Exception 
    */
    @PreAuthorize("hasAuthority('trade:ware:add')")
    @RequestMapping("/save")
    public HttpResult save(@RequestBody Ware model) throws Exception {
//    	setFilePath(model);
        wareService.saveWare(model);
        return HttpResult.ok();
    }
 	/**
    * 修改
    *
    * @param model
    * @return
 	 * @throws Exception 
    */
    @PreAuthorize("hasAuthority('trade:ware:edit')")
    @RequestMapping("/edit")
    public HttpResult edit(@RequestBody Ware model) throws Exception {
//    	setFilePath(model);
        wareService.updateWare(model);
        return HttpResult.ok();
    }
	/**
    * 删除
    *
    * @param model
    * @return
	 * @throws Exception 
    */
    @PreAuthorize("hasAuthority('trade:ware:del')")
    @RequestMapping("/delete")
    public HttpResult delete(@RequestBody Ware model) throws Exception {
        wareService.delWare(model);
        return HttpResult.ok();
    }
    /**
    * 查询详情
    *
    * @param id
    * @return
     * @throws Exception 
    */
    @PreAuthorize("hasAuthority('trade:ware:view')")
    @RequestMapping("/getById")
    public HttpResult getById(@RequestBody Ware ware) throws Exception {
     Ware model = wareService.getById(ware.getWareno());
        // todo 再包装一层
     if(model ==null) {
    	 throw new Exception("商品不存在");
     }
     
		/*
		 * String path = model.getFilepath(); String content = "";
		 * if(!StringUtils.isBlank(path)){ Map<String,Object> paraMap = new HashMap<>();
		 * paraMap.put("path", path); content = HttpClientUtils.doPost(fileServicePath +
		 */
    return HttpResult.ok(model);
    }
    /**
    * 列表查询（非分页）
    *
    * @return
    */
    @RequestMapping("/list")
    public HttpResult list(@RequestBody Ware model) {
        List<Ware> list =  wareService.list(model);
        // todo 再包装一层
        return HttpResult.ok(list);
    }


    /**
    * 列表查询（分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('trade:ware:view')")
    @RequestMapping("/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {

        PageResult page = wareService.findPage(pageRequest);
        // todo 再包装一层
        return HttpResult.ok(page);
    }
    
    /**
     * 列表查询（非分页）
     *
     * @return
     * @throws Exception 
     */
     @PreAuthorize("hasAuthority('trade:ware:edit')")
     @RequestMapping("/attr")
     public HttpResult attr(@RequestBody Ware ware) throws Exception {
         List<WareAttr> list =  wareService.findWareAttr(ware);
         return HttpResult.ok(list);
     }
     
     /**
      * 列表查询（非分页）
      *
      * @return
      * @throws Exception 
      */
      @PreAuthorize("hasAuthority('trade:ware:edit')")
      @RequestMapping("/para")
      public HttpResult para(@RequestBody Ware ware) throws Exception {
    	  List<Basepara> list = wareService.findWareParas(ware);
          return HttpResult.ok(list);
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
     
     private void setFilePath(Ware model)throws Exception{
    	 if(!StringUtils.isBlank(model.getContent())){
     		log.info("上传商品信息:"+fileServicePath);
     		Map<String, Object> paraMap = new HashMap<>();	
     		paraMap.put("type", "html");
     		String content = fillContent(model.getContent(), model.getWarename());
     		paraMap.put("content", content);
     		paraMap.put("path", model.getFilepath());
     		String str = HttpClientUtils.doPost(fileServicePath+"/uploadContent", paraMap);
     		JSONObject ret = JSONObject.parseObject(str);
     		if("0".equals(ret.get("error").toString())){
     			String url = ret.get("url").toString();
     			model.setFilepath(url);
     		}else{
     			throw new Exception(ret.get("msg").toString());
     		}
     	}
     }
}

package com.hml.mall.controller.spread;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hml.core.http.HttpResult;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.spread.ActiveInfo;
import com.hml.mall.iface.spread.IActiveInfoService;
import com.hml.utils.PoiUtils;
import com.hml.utils.StringUtils;


/**
*  前端控制器
* @author hml
* @since 2022-07-31
*/


@RestController
@RequestMapping("/activeInfo" )
public class ActiveInfoController {

    @Autowired
    private IActiveInfoService  activeInfoService;

    /**
    * 保存
    *
    * @param model
    * @return
     * @throws Exception 
    */
    @PreAuthorize("hasAuthority('spread:activeInfo:add')")
    @RequestMapping("/save")
    public HttpResult save(@RequestBody ActiveInfo model) throws Exception {
        activeInfoService.saveActiveInfo(model);
        return HttpResult.ok();
    }
 	/**
    * 修改
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('spread:activeInfo:edit')")
    @RequestMapping("/edit")
    public HttpResult edit(@RequestBody ActiveInfo model) {
        activeInfoService.updateById(model);
        return HttpResult.ok();
    }
    
    /**
     * 修改
     *
     * @param model
     * @return
     */
     @PreAuthorize("hasAuthority('spread:activeInfo:sync')")
     @RequestMapping("/sync")
     public HttpResult sync(@RequestBody List<ActiveInfo> models,HttpServletRequest request ) {
    	 StringBuffer buff = new StringBuffer();
     	if(models != null && models.size() > 0){
     		for(ActiveInfo model : models){
     			try {
     		    	activeInfoService.syncActiveInfo(model);
 				} catch (Exception e) {
 					buff.append(model.getActid()).append("-").append(model.getUserno()).append(":").append(e.getMessage()).append("<br/>");
 				}
     		}
     	}
     	if(buff.toString().length() > 0){
     		return HttpResult.error(buff.toString());
     	}
         return HttpResult.ok();
     }
	/**
    * 删除
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('spread:activeInfo:del')")
    @RequestMapping("/delete")
    public HttpResult delete(@RequestBody ActiveInfo model) {
    	QueryWrapper<ActiveInfo> qw = new QueryWrapper<ActiveInfo>();
    	qw.eq("actid", model.getActid());
    	qw.eq("userno", model.getUserno());
        activeInfoService.remove(qw);
        return HttpResult.ok();
    }
    
    @PreAuthorize("hasAuthority('spread:activeInfo:del')")
    @RequestMapping("/batchDel")
    public HttpResult batchDel(@RequestBody List<ActiveInfo> models,HttpServletRequest request ) {
   	 	 for(ActiveInfo item: models) {
   	 		QueryWrapper<ActiveInfo> qw = new QueryWrapper<ActiveInfo>();
   	    	qw.eq("actid", item.getActid());
   	    	qw.eq("userno", item.getUserno());
   	        activeInfoService.remove(qw);
   	 	 }
        return HttpResult.ok();
    }
    /**
    * 查询详情
    *
    * @param id
    * @return
    */
    @PreAuthorize("hasAuthority('spread:activeInfo:view')")
    @RequestMapping("/getById")
    public HttpResult getById(Integer id) {
     ActiveInfo model = activeInfoService.getById(id);
        // todo 再包装一层
        return HttpResult.ok(model);
    }


    /**
    * 列表查询（非分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('spread:activeInfo:view')")
    @RequestMapping("/list")
    public HttpResult list() {
        ActiveInfo model = new  ActiveInfo();
        List<ActiveInfo> list =  activeInfoService.list(model);
        // todo 再包装一层
        return HttpResult.ok(list);
    }


    /**
    * 列表查询（分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('spread:activeInfo:view')")
    @RequestMapping("/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {

        PageResult page = activeInfoService.findPage(pageRequest);
        // todo 再包装一层
        return HttpResult.ok(page);
    }

    @RequestMapping("/findUser")
    public HttpResult findUser(@RequestBody PageRequest pageRequest) throws Exception {

        PageResult page = activeInfoService.findUser(pageRequest);
        // todo 再包装一层
        return HttpResult.ok(page);
    }
    
    @PreAuthorize("hasAuthority('spread:activeInfo:import')")
    @RequestMapping("/importData")
    public HttpResult importData(@RequestParam("file") MultipartFile file,HttpServletRequest request) throws Exception {
	   	 String[][] str = PoiUtils.imporData(file);
	   	 List<ActiveInfo> data = getData(str);
	   	  
	   	 activeInfoService.saveBatch(data);
        return HttpResult.ok();
    }
    
    private List<ActiveInfo> getData(String[][] str)throws Exception{
    	List<ActiveInfo> data = new ArrayList<ActiveInfo>();
    	for(String[] para:str) {
    		if(para.length != 3) continue;
    		if(StringUtils.isBlank(para[0])
    			|| 	StringUtils.isBlank(para[1]) ) {
    			continue;
    		}
    		ActiveInfo item = new ActiveInfo();
    		 item.setActid(Long.parseLong(para[0]));
    		 item.setUserno(para[1]);
    		 item.setNum(Integer.parseInt(para[2]));
    		data.add(item);
    	}
    	
    	return data;
    }
}

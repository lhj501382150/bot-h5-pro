package com.hml.mall.controller.user;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hml.backcore.req.REQ518;
import com.hml.core.http.HttpResult;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.user.User;
import com.hml.mall.entity.user.UserLandinfo;
import com.hml.mall.iface.user.IUserLandinfoService;
import com.hml.mall.iface.user.IUserService;
import com.hml.mall.util.IPUtils;
import com.hml.utils.DateTimeUtils;
import com.hml.utils.PoiUtils;
import com.hml.utils.StringUtils;


/**
*  前端控制器
* @author hml
* @since 2021-12-27
*/


@RestController
@RequestMapping("/userLandinfo" )
public class UserLandinfoController {

    @Autowired
    private IUserLandinfoService  userLandinfoService;

    @Autowired
    private IUserService userService;

    /**
    * 保存
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('trade:landinfo:add')")
    @RequestMapping("/save")
    public HttpResult save(@RequestBody UserLandinfo model) {
    	UserLandinfo item = userLandinfoService.getById(model.getArtid());
    	if(item != null){
    		return HttpResult.error("资产编号已存在！");
    	}
    	User user = userService.getById(model.getUserno());
    	if(user == null) {
    		return HttpResult.error("客户不存在！");
    	}
    	model.setAstatus("0");
        userLandinfoService.save(model);
        return HttpResult.ok();
    }
 	/**
    * 修改
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('trade:landinfo:edit')")
    @RequestMapping("/edit")
    public HttpResult edit(@RequestBody UserLandinfo model) {
    	UserLandinfo item = userLandinfoService.getById(model.getArtid());
    	if(item == null){
    		return HttpResult.error("请输入正确资产编号！");
    	}
    	User user = userService.getById(model.getUserno());
    	if(user == null) {
    		return HttpResult.error("客户不存在！");
    	}
    	if(!"0".equals(model.getAstatus())){
    		return HttpResult.error("当前资产不能修改！");
    	}
        userLandinfoService.updateById(model);
        return HttpResult.ok();
    }
	/**
    * 删除
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('trade:landinfo:del')")
    @RequestMapping("/delete")
    public HttpResult delete(@RequestBody UserLandinfo model) {
    	UserLandinfo item = userLandinfoService.getById(model.getArtid());
    	if(item == null){
    		return HttpResult.error("请输入正确资产编号！");
    	}
    	if(!"0".equals(model.getAstatus())){
    		return HttpResult.error("当前资产不允许删除！");
    	}
        userLandinfoService.removeById(model);
        return HttpResult.ok();
    }
    @PreAuthorize("hasAuthority('trade:landinfo:issue')")
    @RequestMapping("/issue")
    public HttpResult issue(@RequestBody UserLandinfo model,HttpServletRequest request) throws Exception {
    	UserLandinfo item = userLandinfoService.getById(model.getArtid());
    	if(item == null){
    		return HttpResult.error("请输入正确资产编号！");
    	}
 
    	if(!"0".equals(item.getAstatus())){
    		return HttpResult.error("请勿重复操作！");
    	}
    	REQ518 req = new REQ518();
    	req.setLOGINNO(item.getUserno());
    	req.setUSERNO(item.getUserno());
    	req.setDETYPE("4");
    	req.setDEVICENO("");
    	req.setIP(IPUtils.getIpAddr(request));
    	req.setOPTYPE("1");
    	req.setARTID(String.valueOf(item.getArtid()));
    	
        userLandinfoService.issue(req);
        return HttpResult.ok();
    }
    @PreAuthorize("hasAuthority('trade:landinfo:termination')")
    @RequestMapping("/termination")
    public HttpResult termination(@RequestBody UserLandinfo model,HttpServletRequest request) throws Exception {
    	UserLandinfo item = userLandinfoService.getById(model.getArtid());
    	if(item == null){
    		return HttpResult.error("请输入正确资产编号！");
    	}
    	if(!("1".equals(item.getAstatus()) || "2".equals(item.getAstatus()))){
    		return HttpResult.error("当前状态暂不能执行此操作！");
    	}
    	REQ518 req = new REQ518();
    	req.setLOGINNO(item.getUserno());
    	req.setUSERNO(item.getUserno());
    	req.setDETYPE("4");
    	req.setDEVICENO("");
    	req.setIP(IPUtils.getIpAddr(request));
    	req.setOPTYPE("3");
    	req.setARTID(String.valueOf(item.getArtid()));
        return HttpResult.ok();
    }
    
    @PreAuthorize("hasAuthority('trade:landinfo:import')")
    @RequestMapping("/importData")
    public HttpResult importData(@RequestParam("file") MultipartFile file,HttpServletRequest request) throws Exception {
	   	 String[][] str = PoiUtils.imporData(file);
	   	 List<UserLandinfo> data = getData(str);
	   	 userLandinfoService.remove(null);
	   	 userLandinfoService.saveBatch(data);
        return HttpResult.ok();
    }
    /**
    * 查询详情
    *
    * @param id
    * @return
    */
    @PreAuthorize("hasAuthority('trade:landinfo:view')")
    @RequestMapping("/getById")
    public HttpResult getById(Integer id) {
    	UserLandinfo model = userLandinfoService.getById(id);
        // todo 再包装一层
        return HttpResult.ok(model);
    }


    /**
    * 列表查询（非分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('trade:landinfo:view')")
    @RequestMapping("/list")
    public HttpResult list(@RequestBody UserLandinfo model) {
        List<UserLandinfo> list =  userLandinfoService.list(model);
        PageRequest request = new PageRequest();
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("ATYPE", "3");
        request.setPageNum(1);
        request.setPageSize(1);
        
        PageResult page = userLandinfoService.findPage(request);
        List<UserLandinfo> content = (List<UserLandinfo>)page.getContent();
        
        Map<String,Object> ret = new HashMap<String, Object>();
       ret.put("items", list);
       if(content.size() > 0) {
    	   ret.put("one",content.get(0));
       }
        return HttpResult.ok(ret);
    }


    /**
    * 列表查询（分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('trade:landinfo:view')")
    @RequestMapping("/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {

        PageResult page = userLandinfoService.findPage(pageRequest);
        // todo 再包装一层
        return HttpResult.ok(page);
    }
    
    private List<UserLandinfo> getData(String[][] str)throws Exception{
    	List<UserLandinfo> data = new ArrayList<UserLandinfo>();
    	for(String[] para:str) {
    		if(para.length != 9) continue;
    		if(StringUtils.isBlank(para[1])
    			|| 	StringUtils.isBlank(para[2])
    			|| 	StringUtils.isBlank(para[3])) {
    			continue;
    		}
    		UserLandinfo item = new UserLandinfo();
    		item.setUserno(para[0]);
    		item.setCoordx(para[1]);
    		item.setCoordy(para[2]);
    		item.setArea(para[3]);
    		item.setMem(para[7]);
    		item.setAtypeVal(para[4]);
    		item.setAstatus("0");
    		item.setColor(para[5]);
    		if(StringUtils.isBlank(para[6])) {
    			item.setDate(DateTimeUtils.getCurrentDate("yyyy-MM-dd"));
    		}else {
    			item.setDate(para[6]);
    		}
    		item.setBlocknum(para[8]);
    		data.add(item);
    	}
    	
    	return data;
    }
}

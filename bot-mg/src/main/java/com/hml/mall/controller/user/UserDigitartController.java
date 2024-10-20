package com.hml.mall.controller.user;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hml.backcore.req.REQ518;
import com.hml.core.http.HttpResult;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.user.User;
import com.hml.mall.entity.user.UserDigitart;
import com.hml.mall.iface.user.IUserDigitartService;
import com.hml.mall.iface.user.IUserService;
import com.hml.mall.util.IPUtils;
import com.hml.utils.StringUtils;


/**
*  前端控制器
* @author hml
* @since 2021-09-22
*/


@RestController
@RequestMapping("/userDigitart" )
public class UserDigitartController {

    @Autowired
    private IUserDigitartService  userDigitartService;
    
    @Autowired
    private IUserService userService;

    /**
    * 保存
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('trade:digitart:add')")
    @RequestMapping("/save")
    public HttpResult save(@RequestBody UserDigitart model) {
    	UserDigitart item = userDigitartService.getById(model.getArtid());
    	if(item != null){
    		return HttpResult.error("资产编号已存在！");
    	}
    	User user = userService.getById(model.getUserno());
    	if(user == null) {
    		return HttpResult.error("客户不存在！");
    	}
    	model.setOrguserno(model.getUserno());
 
    	model.setCardstatus("0");
        userDigitartService.save(model);
        model.setOrgartid(String.valueOf(model.getArtid()));
        userDigitartService.updateById(model);
        return HttpResult.ok();
    }
    
    /**
     * 保存
     *
     * @param model
     * @return
     */
     @PreAuthorize("hasAuthority('trade:digitart:add')")
     @RequestMapping("/batchSave")
     public HttpResult batchSave(@RequestBody UserDigitart model) {
     	 
     	User user = userService.getById(model.getUserno());
     	if(user == null) {
     		return HttpResult.error("客户不存在！");
     	}
     	model.setOrguserno(model.getUserno());
     	model.setCardstatus("0");
     	List<UserDigitart> entityList = new ArrayList<UserDigitart>();
     	String spath = model.getSpath();
     	if(StringUtils.isBlank(spath)){
     		return HttpResult.error("请上传图片！");
     	}
     	String[] paths = spath.split(",");
     	int i = userDigitartService.getMaxNo(model.getCardlength(), model.getCardpre());
     	int num = model.getNum();
     	for(String path : paths){
     		if(StringUtils.isBlank(path)) continue;
     		 for(int m=0; m < num;m++){
     			 UserDigitart item = model.clone();
     			 String cardNo = model.getCardpre() + String.format("%0"+model.getCardlength()+"d", i);
     			 item.setCardno(cardNo);
     			 item.setSpath(path);
     			 item.setNum(1);
     			 entityList.add(item);
     			 i++;
     		 }
     	}
     	userDigitartService.saveBatch(entityList);
     	for(UserDigitart item: entityList){
     		item.setOrgartid(String.valueOf(item.getArtid()));
     	}
     	userDigitartService.updateBatchById(entityList);
         return HttpResult.ok();
     }
 	/**
    * 修改
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('trade:digitart:edit')")
    @RequestMapping("/edit")
    public HttpResult edit(@RequestBody UserDigitart model) {
    	UserDigitart item = userDigitartService.getById(model.getArtid());
    	if(item == null){
    		return HttpResult.error("请输入正确资产编号！");
    	}
    	User user = userService.getById(model.getUserno());
    	if(user == null) {
    		return HttpResult.error("客户不存在！");
    	}
    	if(model.getOrgartid() != null) {
    		item = userDigitartService.getById(model.getOrgartid());
    		if(item == null) {
    			return HttpResult.error("请输入正确的原始资产编号");
    		}
    	}
    	if(model.getOrguserno() != null) {
    		user = userService.getById(model.getUserno());
    		if(user == null) {
        		return HttpResult.error("请输入正确的原始发行方！");
        	}
    	}
    	if(!"0".equals(model.getCardstatus())){
    		return HttpResult.error("当前资产不能修改！");
    	}
        userDigitartService.updateById(model);
        return HttpResult.ok();
    }
	/**
    * 删除
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('trade:digitart:del')")
    @RequestMapping("/delete")
    public HttpResult delete(@RequestBody UserDigitart model) {
    	UserDigitart item = userDigitartService.getById(model.getArtid());
    	if(item == null){
    		return HttpResult.error("请输入正确资产编号！");
    	}
    	if(!("0".equals(model.getCardstatus()) || "3".equals(model.getCardstatus()))){
    		return HttpResult.error("当前资产不允许删除！");
    	}
        userDigitartService.removeById(model);
        return HttpResult.ok();
    }
    
    @PreAuthorize("hasAuthority('firm:member:batchDel')")
    @RequestMapping("/batchDel")
    public HttpResult batchDel(@RequestBody List<UserDigitart> list) {
    	  StringBuffer buff = new StringBuffer();
    	  for(UserDigitart model : list) {
    		  try {
    			UserDigitart item = userDigitartService.getById(model.getArtid());
		    	if(item == null){
		    		throw new Exception("请输入正确资产编号！");
		    	}
		    	if(!("0".equals(item.getCardstatus()) || "3".equals(item.getCardstatus()))){
		    		throw new Exception("当前资产不允许删除！");
		    	}
		        userDigitartService.removeById(model);
			} catch (Exception e) {
				buff.append(model.getArtid() + ":" + e.getMessage()).append(" ,");
			}
    	  }
    	  if(buff.toString().length() > 0) {
    		  return HttpResult.error("未全部删除成功，请刷新页面查看最终结果：" + buff.toString());
    	  }else {
    		  return HttpResult.ok();
    	  }
    }
    
    @PreAuthorize("hasAuthority('firm:member:clear')")
    @RequestMapping("/clear")
    public HttpResult clear() {
    	 try {
 			 QueryWrapper<UserDigitart> qw = new QueryWrapper<UserDigitart>();
 			 qw.eq("CARDSTATUS", "0");
 			 
 			 userDigitartService.remove(qw);
 			 
 			return HttpResult.ok();
 		} catch (Exception e) {
 			e.printStackTrace();
 			return HttpResult.error(e.getMessage());
 		}
    }
    
    
    @PreAuthorize("hasAuthority('trade:digitart:issue')")
    @RequestMapping("/issue")
    public HttpResult issue(@RequestBody UserDigitart model,HttpServletRequest request) throws Exception {
    	UserDigitart item = userDigitartService.getById(model.getArtid());
    	if(item == null){
    		return HttpResult.error("请输入正确资产编号！");
    	}
 
    	if(!"0".equals(item.getCardstatus())){
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
    	req.setNUM("1");
    	
        userDigitartService.post(req);
        return HttpResult.ok();
    }
    
    @PreAuthorize("hasAuthority('trade:digitart:issue')")
    @RequestMapping("/batchIssue")
    public HttpResult batchIssue(@RequestBody List<UserDigitart> models,HttpServletRequest request) throws Exception {
    	StringBuffer buff = new StringBuffer();
    	if(models != null && models.size() > 0){
    		for(UserDigitart model : models){
    			try {
    				UserDigitart item = userDigitartService.getById(model.getArtid());
    		    	if(item == null){
    		    		throw new Exception("请输入正确资产编号！");
    		    	}
    		 
    		    	if(!"0".equals(item.getCardstatus())){
    		    		throw new Exception("请勿重复操作！");
    		    	}
    		    	REQ518 req = new REQ518();
    		    	req.setLOGINNO(item.getUserno());
    		    	req.setUSERNO(item.getUserno());
    		    	req.setDETYPE("4");
    		    	req.setDEVICENO("");
    		    	req.setIP(IPUtils.getIpAddr(request));
    		    	req.setOPTYPE("1");
    		    	req.setARTID(String.valueOf(item.getArtid()));
    		    	req.setNUM("1");
    		    	
    		        userDigitartService.post(req);
				} catch (Exception e) {
					buff.append(model.getArtid()).append(":").append(e.getMessage()).append(" ");
				}
    		}
    	}
    	if(buff.toString().length() > 0){
    		return HttpResult.error(buff.toString());
    	}
        return HttpResult.ok();
    }
    
    @PreAuthorize("hasAuthority('trade:digitart:lock')")
    @RequestMapping("/lock")
    public HttpResult lock(@RequestBody List<UserDigitart> models,HttpServletRequest request) throws Exception {
    	StringBuffer buff = new StringBuffer();
    	if(models != null && models.size() > 0){
    		for(UserDigitart model : models){
    			try {
    				UserDigitart item = userDigitartService.getById(model.getArtid());
    		    	if(item == null){
    		    		throw new Exception("请输入正确资产编号！");
    		    	}
    		    	REQ518 req = new REQ518();
    		    	req.setLOGINNO(item.getUserno());
    		    	req.setUSERNO(item.getUserno());
    		    	req.setDETYPE("4");
    		    	req.setDEVICENO("");
    		    	req.setIP(IPUtils.getIpAddr(request));
    		    	req.setOPTYPE("8");
    		    	req.setARTID(String.valueOf(item.getArtid()));
    		    	req.setNUM("1");
    		        userDigitartService.post(req);
				} catch (Exception e) {
					buff.append(model.getArtid()).append(":").append(e.getMessage()).append(" ");
				}
    		}
    	}
    	if(buff.toString().length() > 0){
    		return HttpResult.error(buff.toString());
    	}
        return HttpResult.ok();
    }
    @PreAuthorize("hasAuthority('trade:digitart:unlock')")
    @RequestMapping("/unlock")
    public HttpResult unlock(@RequestBody List<UserDigitart> models,HttpServletRequest request) throws Exception {
    	StringBuffer buff = new StringBuffer();
    	if(models != null && models.size() > 0){
    		for(UserDigitart model : models){
    			try {
    				UserDigitart item = userDigitartService.getById(model.getArtid());
    		    	if(item == null){
    		    		throw new Exception("请输入正确资产编号！");
    		    	}
    		    	REQ518 req = new REQ518();
    		    	req.setLOGINNO(item.getUserno());
    		    	req.setUSERNO(item.getUserno());
    		    	req.setDETYPE("4");
    		    	req.setDEVICENO("");
    		    	req.setIP(IPUtils.getIpAddr(request));
    		    	req.setOPTYPE("9");
    		    	req.setARTID(String.valueOf(item.getArtid()));
    		    	req.setNUM("1");
    		        userDigitartService.post(req);
				} catch (Exception e) {
					buff.append(model.getArtid()).append(":").append(e.getMessage()).append(" ");
				}
    		}
    	}
    	if(buff.toString().length() > 0){
    		return HttpResult.error(buff.toString());
    	}
        return HttpResult.ok();
    }
    @PreAuthorize("hasAuthority('trade:digitart:termination')")
    @RequestMapping("/termination")
    public HttpResult termination(@RequestBody UserDigitart model,HttpServletRequest request) throws Exception {
    	UserDigitart item = userDigitartService.getById(model.getArtid());
    	if(item == null){
    		return HttpResult.error("请输入正确资产编号！");
    	}
    	if(!("1".equals(item.getCardstatus()) || "2".equals(item.getCardstatus()))){
    		return HttpResult.error("当前状态暂不能执行此操作！");
    	}
    	REQ518 req = new REQ518();
    	req.setLOGINNO(item.getUserno());
    	req.setUSERNO(item.getUserno());
    	req.setOPUSERNO(item.getUserno());
    	req.setDETYPE("4");
    	req.setDEVICENO("");
    	req.setIP(IPUtils.getIpAddr(request));
    	req.setOPTYPE("3");
    	req.setARTID(String.valueOf(item.getArtid()));
    	req.setNUM("1");
    	
    	userDigitartService.post(req);
        return HttpResult.ok();
    }
    @PreAuthorize("hasAuthority('trade:digitart:batchTermination')")
    @RequestMapping("/batchTermination")
    public HttpResult batchTermination(@RequestBody List<UserDigitart> models,HttpServletRequest request) throws Exception {
    	StringBuffer buff = new StringBuffer();
    	if(models != null && models.size() > 0){
    		for(UserDigitart model : models){
    			try {
    				UserDigitart item = userDigitartService.getById(model.getArtid());
    		    	if(item == null){
    		    		throw new Exception("请输入正确资产编号！");
    		    	}
    		 
    		    	if(!("1".equals(item.getCardstatus()) || "2".equals(item.getCardstatus()))){
    		    		return HttpResult.error("当前状态暂不能执行此操作！");
    		    	}
    		    	REQ518 req = new REQ518();
    		    	req.setLOGINNO(item.getUserno());
    		    	req.setUSERNO(item.getUserno());
    		    	req.setOPUSERNO(item.getUserno());
    		    	req.setDETYPE("4");
    		    	req.setDEVICENO("");
    		    	req.setIP(IPUtils.getIpAddr(request));
    		    	req.setOPTYPE("3");
    		    	req.setARTID(String.valueOf(item.getArtid()));
    		    	req.setNUM("1");
    		    	
    		    	userDigitartService.post(req);
				} catch (Exception e) {
					buff.append(model.getArtid()).append(":").append(e.getMessage()).append(" ");
				}
    		}
    	}
    	if(buff.toString().length() > 0){
    		return HttpResult.error(buff.toString());
    	}
        return HttpResult.ok();
    }
    @PreAuthorize("hasAuthority('trade:digitart:update')")
    @RequestMapping("/update")
    public HttpResult update(@RequestBody UserDigitart model,HttpServletRequest request) throws Exception {
    	UserDigitart item = userDigitartService.getById(model.getArtid());
    	if(item == null){
    		return HttpResult.error("请输入正确资产编号！");
    	}
 
    	if(!"3".equals(item.getUpstatus())){
    		return HttpResult.error("当前状态，暂不能手工上链！");
    	}
    	REQ518 req = new REQ518();
    	req.setLOGINNO(item.getUserno());
    	req.setUSERNO(item.getUserno());
    	req.setDETYPE("4");
    	req.setDEVICENO("");
    	req.setIP(IPUtils.getIpAddr(request));
    	req.setOPTYPE("5");
    	req.setARTID(String.valueOf(item.getArtid()));
	    req.setNUM("1");
    	
        userDigitartService.post(req);
        return HttpResult.ok();
    }
    @PreAuthorize("hasAuthority('trade:digitart:send')")
    @RequestMapping("/send")
    public HttpResult send(@RequestBody Map<String,String> model,HttpServletRequest request) throws Exception {
    	if(StringUtils.isBlank(model.get("ARTID"))){
    		return HttpResult.error("资产编号不能为空！");
    	}
    	if(StringUtils.isBlank(model.get("USERNO"))){
    		return HttpResult.error("客户编号不能为空！");
    	}
    	if(StringUtils.isBlank(model.get("OPTTYPE"))){
    		return HttpResult.error("操作类型不能为空！");
    	}
    	String artId = model.get("ARTID");
    	String userNo = model.get("USERNO");
    	String optType = model.get("OPTTYPE");
    	String[] artIds = artId.split(",");
    	StringBuffer buff = new StringBuffer();
    	for(String id : artIds){
    		if(StringUtils.isBlank(id)) continue;
    		try {
    			UserDigitart item = userDigitartService.getById(id);
    	    	if(item == null){
    	    		return HttpResult.error("请输入正确资产编号！");
    	    	}
		    	REQ518 req = new REQ518();
		    	req.setLOGINNO(item.getUserno());
		    	req.setUSERNO(item.getUserno());
		    	req.setDETYPE("4");
		    	req.setDEVICENO("");
		    	req.setIP(IPUtils.getIpAddr(request));
		    	req.setOPTYPE(optType);
		    	req.setARTID(id);
		    	req.setNUM("1");
		    	req.setOPUSERNO(userNo);
		    	
		        userDigitartService.post(req);
			} catch (Exception e) {
				buff.append(id).append(":").append(e.getMessage()).append(" ");
			}
    	}
    	if(buff.toString().length() > 0){
    		return HttpResult.error(buff.toString());
    	}
        return HttpResult.ok();
    }
    /**
    * 查询详情
    *
    * @param id
    * @return
    */
    @PreAuthorize("hasAuthority('trade:digitart:view')")
    @RequestMapping("/getById")
    public HttpResult getById(Integer id) {
     UserDigitart model = userDigitartService.getById(id);
        // todo 再包装一层
        return HttpResult.ok(model);
    }


    /**
    * 列表查询（非分页）
    *
    * @return
    */
    @RequestMapping("/list")
    public HttpResult list(@RequestBody UserDigitart model) {
        List<UserDigitart> list =  userDigitartService.list(model);
        // todo 再包装一层
        return HttpResult.ok(list);
    }


    /**
    * 列表查询（分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('trade:digitart:view')")
    @RequestMapping("/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {

        PageResult page = userDigitartService.findPage(pageRequest);
        // todo 再包装一层
        return HttpResult.ok(page);
    }

}

package com.hml.mall.controller.game;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hml.backcore.utils.ZbUtils;
import com.hml.core.http.HttpResult;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.game.Activity;
import com.hml.mall.entity.user.User;
import com.hml.mall.entity.user.UserRelation;
import com.hml.mall.iface.game.IActivityService;
import com.hml.mall.iface.user.IUserRelationService;
import com.hml.mall.iface.user.IUserService;
import com.hml.mall.security.LoginUser;
import com.hml.mall.util.SecurityUtils;
import com.hml.utils.StringUtils;


/**
*  前端控制器
* @author hml
* @since 2021-06-09
*/


@RestController
@RequestMapping("/activity" )
public class ActivityController {

    @Autowired
    private IActivityService  activityService;
    
    @Autowired
    private IUserService userService;
    
    @Autowired
    private IUserRelationService userRelationService;

    /**
    * 保存
    *
    * @param model
    * @return
     * @throws Exception 
    */
    @PreAuthorize("hasAnyAuthority('gm:activity:add','gm:activity:addLike')")
    @RequestMapping("/save")
    public HttpResult save(@RequestBody Activity model) throws Exception {
    	LoginUser loginUser = SecurityUtils.getLoginInfo();
    	if(loginUser.getType() > 0){
    		String userKey = loginUser.getQueryNo();
    		QueryWrapper<UserRelation> qw = new QueryWrapper<>();
    		qw.eq(userKey, loginUser.getUserno());
    		qw.eq("userno", model.getUserno());
    		int count = userRelationService.count(qw);
    		if(count <=0){
    			return HttpResult.error("主持人不存在，请重新输入！");
    		}
    	}else{
    		User user = userService.getById(model.getUserno());
        	if(user == null) {
        		return HttpResult.error("主持人编号不存在！");
        	}
    	}
    	if(model.getActno() != null) {
    		activityService.saveLike(model);
    	}else {
    		activityService.save(model);
    	}
        return HttpResult.ok();
    }
 	/**
    * 修改
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('gm:activity:edit')")
    @RequestMapping("/edit")
    public HttpResult edit(@RequestBody Activity model) {
    	LoginUser loginUser = SecurityUtils.getLoginInfo();
    	if(loginUser.getType() > 0){
    		String userKey = loginUser.getQueryNo();
    		QueryWrapper<UserRelation> qw = new QueryWrapper<>();
    		qw.eq(userKey, loginUser.getUserno());
    		qw.eq("userno", model.getUserno());
    		int count = userRelationService.count(qw);
    		if(count <=0){
    			return HttpResult.error("主持人不存在，请重新输入！");
    		}
    	}else{
    		User user = userService.getById(model.getUserno());
        	if(user == null) {
        		return HttpResult.error("主持人编号不存在！");
        	}
    	}
    	Activity activity = activityService.getById(model.getActno());
	   	 if(activity.getAstatus() == 2) {
	   		 return HttpResult.error("当前活动奖励已发放，暂不能修改！");
	   	 }
        activityService.updateById(model);
        return HttpResult.ok();
    }
	/**
    * 删除
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('gm:activity:del')")
    @RequestMapping("/delete")
    public HttpResult delete(@RequestBody Activity model) {
    	LoginUser loginUser = SecurityUtils.getLoginInfo();
    	Activity activity = activityService.getById(model.getActno());
    	if(loginUser.getType() > 0){
    		String userKey = loginUser.getQueryNo();
    		QueryWrapper<UserRelation> qw = new QueryWrapper<>();
    		qw.eq(userKey, loginUser.getUserno());
    		qw.eq("userno", activity.getUserno());
    		int count = userRelationService.count(qw);
    		if(count <=0){
    			return HttpResult.error("数据不存在，请重新输入！");
    		}
    	}
    	 if(activity.getAstatus() == 2) {
    		 return HttpResult.error("当前活动奖励已发放，暂不能删除！");
    	 }
        activityService.removeById(model);
        return HttpResult.ok();
    }
    /**
     * 审核
     *
     * @param model
     * @return
     * @throws Exception 
     */
     @PreAuthorize("hasAuthority('gm:activity:audit')")
     @RequestMapping("/audit")
     public HttpResult audit(@RequestBody Activity model) throws Exception {
     	LoginUser loginUser = SecurityUtils.getLoginInfo();
    	Activity activity = activityService.getById(model.getActno());
    	if(loginUser.getType() > 0){
    		String userKey = loginUser.getQueryNo();
    		QueryWrapper<UserRelation> qw = new QueryWrapper<>();
    		qw.eq(userKey, loginUser.getUserno());
    		qw.eq("userno", activity.getUserno());
    		int count = userRelationService.count(qw);
    		if(count <=0){
    			return HttpResult.error("数据不存在，请重新输入！");
    		}
    	}
     	 if(activity.getAstatus() != 1) {
     		 return HttpResult.error("请在活动结束后，再执行此操作！");
     	 }
    	 ZbUtils.activtyAudit(model.getActno());
         return HttpResult.ok();
     }
    /**
    * 查询详情
    *
    * @param id
    * @return
    */
    @PreAuthorize("hasAuthority('gm:activity:view')")
    @RequestMapping("/getById")
    public HttpResult getById(Integer id) {
     Activity model = activityService.getById(id);
        // todo 再包装一层
        return HttpResult.ok(model);
    }


    /**
    * 列表查询（非分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('gm:activity:view')")
    @RequestMapping("/list")
    public HttpResult list() {
        Activity model = new  Activity();
        List<Activity> list =  activityService.list(model);
        // todo 再包装一层
        return HttpResult.ok(list);
    }


    /**
    * 列表查询（分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('gm:activity:view')")
    @RequestMapping("/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {

        PageResult page = activityService.findPage(pageRequest);
        // todo 再包装一层
        return HttpResult.ok(page);
    }

}

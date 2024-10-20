package com.hml.mall.controller.user;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hml.core.http.HttpResult;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.user.UserCardinfo;
import com.hml.mall.iface.user.IUserCardinfoService;
import com.hml.mall.security.LoginUser;
import com.hml.mall.util.SecurityUtils;


/**
*  前端控制器
* @author hml
* @since 2021-11-19
*/


@RestController
@RequestMapping("/userCard" )
public class UserCardinfoController {

    @Autowired
    private IUserCardinfoService  userCardinfoService;

    /**
    * 保存
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('money:usercard:add')")
    @RequestMapping("/save")
    public HttpResult save(@RequestBody UserCardinfo model) {
    	QueryWrapper<UserCardinfo> qw = new QueryWrapper<UserCardinfo>();
    	qw.eq("CARDNO", model.getCardno());
    	qw.eq("SERTYPE", "2");
    	int count = userCardinfoService.count(qw);
    	if(count > 0) {
    		return HttpResult.error("该银行卡已存在！");
    	}
    	LoginUser user = SecurityUtils.getLoginInfo();
    	if(user.getType() > 0) {
    		model.setUserno(user.getUserno());
    	}
    	model.setSertype("2");//银行卡
        userCardinfoService.save(model);
        return HttpResult.ok();
    }
 	/**
    * 修改
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('money:usercard:edit')")
    @RequestMapping("/edit")
    public HttpResult edit(@RequestBody UserCardinfo model) {
    	LoginUser user = SecurityUtils.getLoginInfo();
    	if(user.getType() > 0) {
    		model.setUserno(user.getUserno());
    	}
    	model.setSertype("2");//银行卡
        userCardinfoService.updateById(model);
        return HttpResult.ok();
    }
	/**
    * 删除
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('money:usercard:del')")
    @RequestMapping("/delete")
    public HttpResult delete(@RequestBody UserCardinfo model) {
    	QueryWrapper<UserCardinfo> qw = new QueryWrapper<UserCardinfo>();
    	qw.eq("ID", model.getId());
    	qw.eq("SERTYPE", "2");
    	LoginUser user = SecurityUtils.getLoginInfo();
    	if(user.getType() > 0) {
    		qw.eq("USERNO", user.getUserno());
    	}
        userCardinfoService.remove(qw);
        return HttpResult.ok();
    }
    
    /**
     * 删除
     *
     * @param model
     * @return
     */
     @PreAuthorize("hasAuthority('money:usercard:reject')")
     @RequestMapping("/reject")
     public HttpResult reject(@RequestBody UserCardinfo model) {
     	UserCardinfo item = userCardinfoService.getById(model.getId());
     	if(item == null) {
     		return HttpResult.error("数据不存在！");
     	}
     	if("0".equals(item.getSigstatus())){
     		return HttpResult.error("当前数据未签约,暂不能驳回");
     	}else if("1".equals(item.getSigstatus())){
     		return HttpResult.error("当前数据已签约,暂不能驳回");
     	}else if("2".equals(item.getSigstatus())){
     		return HttpResult.error("当前数据签约失败,暂不能驳回");
     	}
     	item.setSigstatus("2");
         userCardinfoService.updateById(item);
         return HttpResult.ok();
     }
    
    @PreAuthorize("hasAuthority('money:usercard:auth')")
    @RequestMapping("/auth")
    public HttpResult auth(@RequestBody UserCardinfo model) throws Exception {
    	userCardinfoService.auth(model);
        return HttpResult.ok();
    }
    /**
    * 查询详情
    *
    * @param id
    * @return
    */
    @PreAuthorize("hasAuthority('money:usercard:view')")
    @RequestMapping("/getById")
    public HttpResult getById(Integer id) {
     UserCardinfo model = userCardinfoService.getById(id);
        // todo 再包装一层
        return HttpResult.ok(model);
    }


    /**
    * 列表查询（非分页）
    *
    * @return
    */
    @RequestMapping("/list")
    public HttpResult list(@RequestBody UserCardinfo model) {
        LoginUser user = SecurityUtils.getLoginInfo();
    	if(user.getType() > 0) {
    		model.setUserno(user.getUserno());
    	}
//    	model.setSertype("2");
        List<UserCardinfo> list =  userCardinfoService.list(model);
        // todo 再包装一层
        return HttpResult.ok(list);
    }


    /**
    * 列表查询（分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('money:usercard:view')")
    @RequestMapping("/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {
    	//pageRequest.getParams().put("SERTYPE", "2");
        PageResult page = userCardinfoService.findPage(pageRequest);
        // todo 再包装一层
        return HttpResult.ok(page);
    }

}

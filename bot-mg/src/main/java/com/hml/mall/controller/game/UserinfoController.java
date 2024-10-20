package com.hml.mall.controller.game;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hml.core.http.HttpResult;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.game.Userinfo;
import com.hml.mall.entity.user.User;
import com.hml.mall.entity.user.UserRelation;
import com.hml.mall.iface.game.IUserinfoService;
import com.hml.mall.iface.user.IUserRelationService;
import com.hml.mall.iface.user.IUserService;
import com.hml.mall.security.LoginUser;
import com.hml.mall.util.SecurityUtils;


/**
*  前端控制器
* @author hml
* @since 2021-06-28
*/


@RestController
@RequestMapping("/userinfo" )
public class UserinfoController {

    @Autowired
    private IUserinfoService  userinfoService;
    
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
    @PreAuthorize("hasAuthority('gm:userinfo:add')")
    @RequestMapping("/save")
    public HttpResult save(@RequestBody Userinfo model) throws Exception {
    	int count = checkUser(model.getUserno());
    	if(count <= 0) {
    		return HttpResult.error("客户不存在");
    	}
        userinfoService.save(model);
        return HttpResult.ok();
    }
 	/**
    * 修改
    *
    * @param model
    * @return
 	 * @throws Exception 
    */
    @PreAuthorize("hasAuthority('gm:userinfo:edit')")
    @RequestMapping("/edit")
    public HttpResult edit(@RequestBody Userinfo model) throws Exception {
    	int count = checkUser(model.getUserno());
    	if(count <= 0) {
    		return HttpResult.error("客户不存在");
    	}
        userinfoService.updateById(model);
        return HttpResult.ok();
    }
	/**
    * 删除
    *
    * @param model
    * @return
	 * @throws Exception 
    */
    @PreAuthorize("hasAuthority('gm:userinfo:del')")
    @RequestMapping("/delete")
    public HttpResult delete(@RequestBody Userinfo model) throws Exception {
    	Userinfo userinfo = userinfoService.getById(model.getUgno());
    	int count = checkUser(userinfo.getUserno());
    	if(count <= 0) {
    		return HttpResult.error("数据不存在");
    	}
        userinfoService.removeById(model);
        return HttpResult.ok();
    }
    /**
    * 查询详情
    *
    * @param id
    * @return
    */
    @PreAuthorize("hasAuthority('gm:userinfo:view')")
    @RequestMapping("/getById")
    public HttpResult getById(Integer id) {
     Userinfo model = userinfoService.getById(id);
        // todo 再包装一层
        return HttpResult.ok(model);
    }


    /**
    * 列表查询（非分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('gm:userinfo:view')")
    @RequestMapping("/list")
    public HttpResult list() {
        Userinfo model = new  Userinfo();
        List<Userinfo> list =  userinfoService.list(model);
        // todo 再包装一层
        return HttpResult.ok(list);
    }


    /**
    * 列表查询（分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('gm:userinfo:view')")
    @RequestMapping("/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {

        PageResult page = userinfoService.findPage(pageRequest);
        // todo 再包装一层
        return HttpResult.ok(page);
    }

    private int checkUser(String userno)throws Exception{
    	LoginUser user = SecurityUtils.getLoginInfo();
    	QueryWrapper<UserRelation> qw = new QueryWrapper<>();
    	qw.eq("userno", userno);
    	if(user.getType() > 0){
    		String userKey = user.getQueryNo();
    		qw.eq(userKey, user.getUserno());
    	}
    	int count = userRelationService.count(qw);
    	return count;
    }
}

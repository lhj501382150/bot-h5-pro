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
import com.hml.mall.entity.game.Userlist;
import com.hml.mall.entity.user.User;
import com.hml.mall.iface.game.IUserlistService;
import com.hml.mall.iface.user.IUserService;


/**
*  前端控制器
* @author hml
* @since 2021-06-09
*/


@RestController
@RequestMapping("/userlist" )
public class UserlistController {

    @Autowired
    private IUserlistService  userlistService;
    
    @Autowired
    private IUserService userService;

    /**
    * 保存
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('gm:userlist:add')")
    @RequestMapping("/save")
    public HttpResult save(@RequestBody Userlist model) {
    	
    	User user = userService.getById(model.getUserno());
    	if(user == null) {
    		return HttpResult.error("客户编号不存在！");
    	}
    	
    	QueryWrapper<Userlist> qw = new QueryWrapper<Userlist>();
    	qw.eq("ACTNO", model.getActno());
    	qw.eq("USERNO", model.getUserno());
    	
    	Userlist one = userlistService.getOne(qw);
    	if(one != null) {
    		return HttpResult.error("该数据已存在");
    	}
    	
        userlistService.save(model);
        return HttpResult.ok();
    }
 	/**
    * 修改
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('gm:userlist:edit')")
    @RequestMapping("/edit")
    public HttpResult edit(@RequestBody Userlist model) {
        userlistService.updateById(model);
        return HttpResult.ok();
    }
	/**
    * 删除
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('gm:userlist:del')")
    @RequestMapping("/delete")
    public HttpResult delete(@RequestBody Userlist model) {
    	QueryWrapper<Userlist> qw = new QueryWrapper<Userlist>();
    	qw.eq("ACTNO", model.getActno());
    	qw.eq("USERNO", model.getUserno());
        userlistService.remove(qw);
        return HttpResult.ok();
    }
    /**
    * 查询详情
    *
    * @param id
    * @return
    */
    @PreAuthorize("hasAuthority('gm:userlist:view')")
    @RequestMapping("/getById")
    public HttpResult getById(Integer id) {
     Userlist model = userlistService.getById(id);
        // todo 再包装一层
        return HttpResult.ok(model);
    }


    /**
    * 列表查询（非分页）
    *
    * @return
    */
    @RequestMapping("/list")
    public HttpResult list(@RequestBody Userlist model) {
        List<Userlist> list =  userlistService.list(model);
        // todo 再包装一层
        return HttpResult.ok(list);
    }


    /**
    * 列表查询（分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('gm:userlist:view')")
    @RequestMapping("/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {

        PageResult page = userlistService.findPage(pageRequest);
        // todo 再包装一层
        return HttpResult.ok(page);
    }

}

package com.hml.mall.controller.mt;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.hml.core.http.HttpResult;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.mt.Guest;
import com.hml.mall.entity.user.User;
import com.hml.mall.iface.mt.IGuestService;
import com.hml.mall.iface.user.IUserService;


/**
*  前端控制器
* @author hml
* @since 2022-05-14
*/


@RestController
@RequestMapping("/guest" )
public class GuestController {

    @Autowired
    private IGuestService  guestService;
    @Autowired
    private IUserService userService;

    /**
    * 保存
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('mt:guest:add')")
    @RequestMapping("/save")
    public HttpResult save(@RequestBody Guest model) {
    	User user = userService.getById(model.getUserno());
    	if(user == null) {
    		return HttpResult.error("嘉宾编号不存在！");
    	}
        guestService.save(model);
        return HttpResult.ok();
    }
 	/**
    * 修改
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('mt:guest:edit')")
    @RequestMapping("/edit")
    public HttpResult edit(@RequestBody Guest model) {
    	User user = userService.getById(model.getUserno());
    	if(user == null) {
    		return HttpResult.error("嘉宾编号不存在！");
    	}
    	UpdateWrapper<Guest> qw = new UpdateWrapper<Guest>();
    	qw.eq("MNO", model.getMno());
    	qw.eq("USERNO", model.getUserno());
        guestService.update(model, qw);
        return HttpResult.ok();
    }
	/**
    * 删除
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('mt:guest:del')")
    @RequestMapping("/delete")
    public HttpResult delete(@RequestBody Guest model) {
    	QueryWrapper<Guest> qw = new QueryWrapper<Guest>();
    	qw.eq("MNO", model.getMno());
    	qw.eq("USERNO", model.getUserno());
        guestService.remove(qw);
        return HttpResult.ok();
    }
    /**
    * 查询详情
    *
    * @param id
    * @return
    */
    @PreAuthorize("hasAuthority('mt:guest:view')")
    @RequestMapping("/getById")
    public HttpResult getById(Integer id) {
     Guest model = guestService.getById(id);
        // todo 再包装一层
        return HttpResult.ok(model);
    }


    /**
    * 列表查询（非分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('mt:guest:view')")
    @RequestMapping("/list")
    public HttpResult list() {
        Guest model = new  Guest();
        List<Guest> list =  guestService.list(model);
        // todo 再包装一层
        return HttpResult.ok(list);
    }


    /**
    * 列表查询（分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('mt:guest:view')")
    @RequestMapping("/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {

        PageResult page = guestService.findPage(pageRequest);
        // todo 再包装一层
        return HttpResult.ok(page);
    }

}

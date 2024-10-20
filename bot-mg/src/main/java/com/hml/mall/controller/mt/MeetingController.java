			package com.hml.mall.controller.mt;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hml.core.http.HttpResult;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.mt.Meeting;
import com.hml.mall.entity.user.User;
import com.hml.mall.iface.mt.IMeetingService;
import com.hml.mall.iface.user.IUserService;


/**
*  前端控制器
* @author hml
* @since 2022-05-14
*/


@RestController
@RequestMapping("/meeting" )
public class MeetingController {

    @Autowired
    private IMeetingService  meetingService;
    @Autowired
    private IUserService userService;

    /**
    * 保存
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('mt:metting:add')")
    @RequestMapping("/save")
    public HttpResult save(@RequestBody Meeting model) {
    	User user = userService.getById(model.getUserno());
    	if(user == null) {
    		return HttpResult.error("主持人编号不存在！");
    	}
        meetingService.save(model);
        return HttpResult.ok();
    }
 	/**
    * 修改
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('mt:metting:edit')")
    @RequestMapping("/edit")
    public HttpResult edit(@RequestBody Meeting model) {
    	User user = userService.getById(model.getUserno());
    	if(user == null) {
    		return HttpResult.error("主持人编号不存在！");
    	}
        meetingService.updateById(model);
        return HttpResult.ok();
    }
	/**
    * 删除
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('mt:metting:del')")
    @RequestMapping("/delete")
    public HttpResult delete(@RequestBody Meeting model) {
        meetingService.removeById(model);
        return HttpResult.ok();
    }
    /**
    * 查询详情
    *
    * @param id
    * @return
    */
    @PreAuthorize("hasAuthority('mt:metting:view')")
    @RequestMapping("/getById")
    public HttpResult getById(Integer id) {
     Meeting model = meetingService.getById(id);
        // todo 再包装一层
        return HttpResult.ok(model);
    }


    /**
    * 列表查询（非分页）
    *
    * @return
    */
    @RequestMapping("/list")
    public HttpResult list() {
        Meeting model = new  Meeting();
        List<Meeting> list =  meetingService.list(model);
        // todo 再包装一层
        return HttpResult.ok(list);
    }


    /**
    * 列表查询（分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('mt:metting:view')")
    @RequestMapping("/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {

        PageResult page = meetingService.findPage(pageRequest);
        // todo 再包装一层
        return HttpResult.ok(page);
    }

}

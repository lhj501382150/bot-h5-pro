package com.hml.mall.controller.user;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hml.core.http.HttpResult;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.user.UserFeedback;
import com.hml.mall.iface.user.IUserFeedbackService;
import com.hml.mall.util.SecurityUtils;
import com.hml.utils.DateTimeUtils;


/**
*  前端控制器
* @author hml
* @since 2021-07-17
*/


@RestController
@RequestMapping("/userFeedback" )
public class UserFeedbackController {

    @Autowired
    private IUserFeedbackService  userFeedbackService;

    /**
    * 保存
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('firm:userfeedback:add')")
    @RequestMapping("/save")
    public HttpResult save(@RequestBody UserFeedback model,HttpServletRequest request) {
    	fillData(model,request);
        userFeedbackService.save(model);
        return HttpResult.ok();
    }
 	/**
    * 修改
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('firm:userfeedback:edit')")
    @RequestMapping("/edit")
    public HttpResult edit(@RequestBody UserFeedback model,HttpServletRequest request) {
    	fillData(model,request);
        userFeedbackService.updateById(model);
        return HttpResult.ok();
    }
	/**
    * 删除
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('firm:userfeedback:del')")
    @RequestMapping("/delete")
    public HttpResult delete(@RequestBody UserFeedback model) {
        userFeedbackService.removeById(model);
        return HttpResult.ok();
    }
    /**
    * 查询详情
    *
    * @param id
    * @return
    */
    @PreAuthorize("hasAuthority('firm:userfeedback:view')")
    @RequestMapping("/getById")
    public HttpResult getById(Integer id) {
     UserFeedback model = userFeedbackService.getById(id);
        // todo 再包装一层
        return HttpResult.ok(model);
    }


    /**
    * 列表查询（非分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('firm:userfeedback:dtl')")
    @RequestMapping("/list")
    public HttpResult list(@RequestBody UserFeedback model) {
        List<UserFeedback> list =  userFeedbackService.list(model);
        // todo 再包装一层
        return HttpResult.ok(list);
    }


    /**
    * 列表查询（分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('firm:userfeedback:view')")
    @RequestMapping("/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {

        PageResult page = userFeedbackService.findPage(pageRequest);
        // todo 再包装一层
        return HttpResult.ok(page);
    }

    private void fillData(UserFeedback data,HttpServletRequest request) {
    	String user = SecurityUtils.getUsername();
    	String time = DateTimeUtils.getDateTime();
    	data.setOpttime(time);
    	data.setOptuser(user);
    }
}

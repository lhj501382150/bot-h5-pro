package com.hml.mall.controller.money;


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
import com.hml.mall.entity.money.UsermoneyChangeApply;
import com.hml.mall.iface.money.IUsermoneyChangeApplyService;
import com.hml.mall.util.IPUtils;
import com.hml.mall.util.SecurityUtils;
import com.hml.utils.DateTimeUtils;


/**
*  前端控制器
* @author hml
* @since 2021-07-11
*/


@RestController
@RequestMapping("/changeApply" )
public class UsermoneyChangeApplyController {

    @Autowired
    private IUsermoneyChangeApplyService  usermoneyChangeApplyService;

    /**
    * 保存
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('money:moneychange:add')")
    @RequestMapping("/save")
    public HttpResult save(@RequestBody UsermoneyChangeApply model) {
    	model.setStatus(0);
        usermoneyChangeApplyService.save(model);
        return HttpResult.ok();
    }
 	/**
    * 修改
    *
    * @param model
    * @return
 	 * @throws Exception 
    */
    @PreAuthorize("hasAuthority('money:changeapply:edit')")
    @RequestMapping("/edit")
    public HttpResult edit(@RequestBody UsermoneyChangeApply model,HttpServletRequest request) throws Exception {
    	model.setChkip(IPUtils.getIpAddr(request));
    	model.setChktime(DateTimeUtils.getDateTime());
    	model.setChkuser(SecurityUtils.getUsername());
    	model.setStatus(1);
        usermoneyChangeApplyService.audit(model);
        return HttpResult.ok();
    }
	/**
    * 删除
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('money:changeapply:del')")
    @RequestMapping("/delete")
    public HttpResult delete(@RequestBody UsermoneyChangeApply model) {
    	UsermoneyChangeApply data = usermoneyChangeApplyService.getById(model.getWartno());
    	if(data.getStatus() != 0) {
    		return HttpResult.error("该数据已处理！");
    	}
        usermoneyChangeApplyService.removeById(model);
        return HttpResult.ok();
    }
    /**
    * 查询详情
    *
    * @param id
    * @return
    */
    @PreAuthorize("hasAuthority('money:changeapply:view')")
    @RequestMapping("/getById")
    public HttpResult getById(Integer id) {
     UsermoneyChangeApply model = usermoneyChangeApplyService.getById(id);
        // todo 再包装一层
        return HttpResult.ok(model);
    }


    /**
    * 列表查询（非分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('money:changeapply:view')")
    @RequestMapping("/list")
    public HttpResult list() {
        UsermoneyChangeApply model = new  UsermoneyChangeApply();
        List<UsermoneyChangeApply> list =  usermoneyChangeApplyService.list(model);
        // todo 再包装一层
        return HttpResult.ok(list);
    }


    /**
    * 列表查询（分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('money:changeapply:view')")
    @RequestMapping("/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {

        PageResult page = usermoneyChangeApplyService.findPage(pageRequest);
        // todo 再包装一层
        return HttpResult.ok(page);
    }

}

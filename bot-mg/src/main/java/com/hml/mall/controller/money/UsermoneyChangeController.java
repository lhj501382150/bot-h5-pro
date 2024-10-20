package com.hml.mall.controller.money;


import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hml.backcore.utils.ZbUtils;
import com.hml.core.http.HttpResult;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.money.UsermoneyChange;
import com.hml.mall.iface.money.IUsermoneyChangeService;


/**
*  前端控制器
* @author hml
* @since 2021-04-14
*/


@RestController
@RequestMapping("/usermoneyChange" )
public class UsermoneyChangeController {

    @Autowired
    private IUsermoneyChangeService  usermoneyChangeService;

    /**
    * 保存
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('money:moneychange:add')")
    @RequestMapping("/save")
    public HttpResult save(@RequestBody UsermoneyChange model) {
    	String userno = model.getUserno();
    	String actno = model.getAcctno();
    	String subno = model.getSubno();
    	String money = model.getChmoney().toString();
    	String contno = model.getExten();
    	String memo = model.getMemo();
        try {
			ZbUtils.changeMoney(userno, actno, subno, money, contno, memo);
		} catch (Exception e) {
			e.printStackTrace();
			return HttpResult.error(e.getMessage());
		}
        return HttpResult.ok();
    }
 	/**
    * 修改
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('money:moneychange:edit')")
    @RequestMapping("/edit")
    public HttpResult edit(@RequestBody UsermoneyChange model) {
        usermoneyChangeService.updateById(model);
        return HttpResult.ok();
    }
	/**
    * 删除
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('money:moneychange:del')")
    @RequestMapping("/delete")
    public HttpResult delete(@RequestBody UsermoneyChange model) {
        usermoneyChangeService.removeById(model);
        return HttpResult.ok();
    }
    /**
    * 查询详情
    *
    * @param id
    * @return
    */
    @PreAuthorize("hasAuthority('money:usermoneyChange:view')")
    @RequestMapping("/getById")
    public HttpResult getById(Integer id) {
     UsermoneyChange model = usermoneyChangeService.getById(id);
        // todo 再包装一层
        return HttpResult.ok(model);
    }


    /**
    * 列表查询（非分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('money:moneychange:view')")
    @RequestMapping("/list")
    public HttpResult list() {
        UsermoneyChange model = new  UsermoneyChange();
        List<UsermoneyChange> list =  usermoneyChangeService.list(model);
        // todo 再包装一层
        return HttpResult.ok(list);
    }


    /**
    * 列表查询（分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('money:moneychange:view')")
    @RequestMapping("/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {

        PageResult page = usermoneyChangeService.findPage(pageRequest);
        // todo 再包装一层
        return HttpResult.ok(page);
    }

    
    @PreAuthorize("hasAuthority('operations:money:view')")
    @RequestMapping("/findScorePage")
    public HttpResult findScorePage(@RequestBody PageRequest pageRequest) {

    	pageRequest.getParams().put("SUBNO@IN", Arrays.asList("100","101"));
        PageResult page = usermoneyChangeService.findPage(pageRequest);
        // todo 再包装一层
        return HttpResult.ok(page);
    }
}

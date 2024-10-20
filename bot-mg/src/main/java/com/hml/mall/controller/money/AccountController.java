package com.hml.mall.controller.money;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hml.core.http.HttpResult;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.money.Account;
import com.hml.mall.iface.money.IAccountService;


/**
*  前端控制器
* @author hml
* @since 2021-04-14
*/


@RestController
@RequestMapping("/account" )
public class AccountController {

    @Autowired
    private IAccountService  accountService;

    /**
    * 保存
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('money:account:add')")
    @RequestMapping("/save")
    public HttpResult save(@RequestBody Account model) {
        accountService.save(model);
        return HttpResult.ok();
    }
 	/**
    * 修改
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('money:account:edit')")
    @RequestMapping("/edit")
    public HttpResult edit(@RequestBody Account model) {
        accountService.updateById(model);
        return HttpResult.ok();
    }
	/**
    * 删除
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('money:account:del')")
    @RequestMapping("/delete")
    public HttpResult delete(@RequestBody Account model) {
        accountService.removeById(model);
        return HttpResult.ok();
    }
    /**
    * 查询详情
    *
    * @param id
    * @return
    */
    @PreAuthorize("hasAuthority('money:account:view')")
    @RequestMapping("/getById")
    public HttpResult getById(Integer id) {
     Account model = accountService.getById(id);
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
        Account model = new  Account();
        List<Account> list =  accountService.list(model);
        // todo 再包装一层
        return HttpResult.ok(list);
    }


    /**
    * 列表查询（分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('money:account:view')")
    @RequestMapping("/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {

        PageResult page = accountService.findPage(pageRequest);
        // todo 再包装一层
        return HttpResult.ok(page);
    }

}

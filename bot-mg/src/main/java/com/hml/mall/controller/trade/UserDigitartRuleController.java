package com.hml.mall.controller.trade;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hml.core.http.HttpResult;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.trade.UserDigitartRule;
import com.hml.mall.entity.trade.WareSc;
import com.hml.mall.iface.trade.IUserDigitartRuleService;
import com.hml.mall.iface.trade.IWareService;


/**
*  前端控制器
* @author hml
* @since 2022-07-18
*/


@RestController
@RequestMapping("/userDigitartRule" )
public class UserDigitartRuleController {

    @Autowired
    private IUserDigitartRuleService  userDigitartRuleService;
    @Autowired
    private IWareService  wareService;

    /**
    * 保存
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('trade:digitartRule:add')")
    @RequestMapping("/save")
    public HttpResult save(@RequestBody UserDigitartRule model) {
        userDigitartRuleService.save(model);
        return HttpResult.ok();
    }
 	/**
    * 修改
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('trade:digitartRule:edit')")
    @RequestMapping("/edit")
    public HttpResult edit(@RequestBody UserDigitartRule model) {
        userDigitartRuleService.updateById(model);
        return HttpResult.ok();
    }
	/**
    * 删除
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('trade:digitartRule:del')")
    @RequestMapping("/delete")
    public HttpResult delete(@RequestBody UserDigitartRule model) {
        userDigitartRuleService.removeById(model);
        return HttpResult.ok();
    }
    /**
    * 查询详情
    *
    * @param id
    * @return
    */
    @PreAuthorize("hasAuthority('trade:digitartRule:view')")
    @RequestMapping("/getById")
    public HttpResult getById(Integer id) {
     UserDigitartRule model = userDigitartRuleService.getById(id);
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
        UserDigitartRule model = new  UserDigitartRule();
        List<UserDigitartRule> list =  userDigitartRuleService.list(model);
        // todo 再包装一层
        return HttpResult.ok(list);
    }


    /**
    * 列表查询（分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('trade:digitartRule:view')")
    @RequestMapping("/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {

        PageResult page = userDigitartRuleService.findPage(pageRequest);
        // todo 再包装一层
        return HttpResult.ok(page);
    }
    
    @RequestMapping("/warelist")
    public HttpResult warelist() throws Exception {
        List<WareSc> list = wareService.findWareSc(null);
        // todo 再包装一层
        return HttpResult.ok(list);
    }

}

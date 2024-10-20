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
import com.hml.mall.entity.trade.WareFirmRule;
import com.hml.mall.entity.trade.WareSc;
import com.hml.mall.iface.trade.IWareFirmRuleService;
import com.hml.mall.iface.trade.IWareService;


/**
*  前端控制器
* @author hml
* @since 2022-07-19
*/


@RestController
@RequestMapping("/wareFirmRule" )
public class WareFirmRuleController {

    @Autowired
    private IWareFirmRuleService  wareFirmRuleService;
    @Autowired
    private IWareService  wareService;

    /**
    * 保存
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('trade:wareFirmRule:add')")
    @RequestMapping("/save")
    public HttpResult save(@RequestBody WareFirmRule model) {
        wareFirmRuleService.save(model);
        return HttpResult.ok();
    }
 	/**
    * 修改
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('trade:wareFirmRule:edit')")
    @RequestMapping("/edit")
    public HttpResult edit(@RequestBody WareFirmRule model) {
        wareFirmRuleService.updateById(model);
        return HttpResult.ok();
    }
	/**
    * 删除
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('trade:wareFirmRule:del')")
    @RequestMapping("/delete")
    public HttpResult delete(@RequestBody WareFirmRule model) {
        wareFirmRuleService.removeById(model);
        return HttpResult.ok();
    }
    /**
    * 查询详情
    *
    * @param id
    * @return
    */
    @PreAuthorize("hasAuthority('trade:wareFirmRule:view')")
    @RequestMapping("/getById")
    public HttpResult getById(Integer id) {
     WareFirmRule model = wareFirmRuleService.getById(id);
        // todo 再包装一层
        return HttpResult.ok(model);
    }


    /**
    * 列表查询（非分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('trade:wareFirmRule:view')")
    @RequestMapping("/list")
    public HttpResult list() {
        WareFirmRule model = new  WareFirmRule();
        List<WareFirmRule> list =  wareFirmRuleService.list(model);
        // todo 再包装一层
        return HttpResult.ok(list);
    }


    /**
    * 列表查询（分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('trade:wareFirmRule:view')")
    @RequestMapping("/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {

        PageResult page = wareFirmRuleService.findPage(pageRequest);
        // todo 再包装一层
        return HttpResult.ok(page);
    }
    
    @RequestMapping("/warelist")
    public HttpResult warelist() throws Exception {
    	WareSc item = new WareSc();
    	item.setFlag(4);
        List<WareSc> list = wareService.findWareSc(item);
        // todo 再包装一层
        return HttpResult.ok(list);
    }

}

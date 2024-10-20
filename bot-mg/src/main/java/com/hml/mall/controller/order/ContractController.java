package com.hml.mall.controller.order;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hml.core.http.HttpResult;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.order.Contract;
import com.hml.mall.iface.order.IContractService;


/**
*  前端控制器
* @author hml
* @since 2021-04-18
*/


@RestController
@RequestMapping("/contract" )
public class ContractController {

    @Autowired
    private IContractService  contractService;

    /**
    * 保存
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('order:contract:add')")
    @RequestMapping("/save")
    public HttpResult save(@RequestBody Contract model) {
        contractService.save(model);
        return HttpResult.ok();
    }
 	/**
    * 修改
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('order:contract:edit')")
    @RequestMapping("/edit")
    public HttpResult edit(@RequestBody Contract model) {
        contractService.updateById(model);
        return HttpResult.ok();
    }
	/**
    * 删除
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('order:contract:del')")
    @RequestMapping("/delete")
    public HttpResult delete(@RequestBody Contract model) {
        contractService.removeById(model);
        return HttpResult.ok();
    }
    /**
    * 查询详情
    *
    * @param id
    * @return
    */
    @PreAuthorize("hasAuthority('order:contract:view')")
    @RequestMapping("/getById")
    public HttpResult getById(Integer id) {
     Contract model = contractService.getById(id);
        // todo 再包装一层
        return HttpResult.ok(model);
    }


    /**
    * 列表查询（非分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('order:contract:view')")
    @RequestMapping("/list")
    public HttpResult list() {
        Contract model = new  Contract();
        List<Contract> list =  contractService.list(model);
        // todo 再包装一层
        return HttpResult.ok(list);
    }


    /**
    * 列表查询（分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('order:contract:view')")
    @RequestMapping("/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {

        PageResult page = contractService.findPage(pageRequest);
        // todo 再包装一层
        return HttpResult.ok(page);
    }

}

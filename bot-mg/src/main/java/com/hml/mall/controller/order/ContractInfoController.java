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
import com.hml.mall.entity.order.Invoic;
import com.hml.mall.entity.order.Sendinfo;
import com.hml.mall.iface.order.IContractService;


/**
*  前端控制器
* @author hml
* @since 2021-04-18
*/


@RestController
@RequestMapping("/contractInfo" )
public class ContractInfoController {

    @Autowired
    private IContractService  contractService;

    /**
    * 保存
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('order:contract:send')")
    @RequestMapping("/editSend")
    public HttpResult editSend(@RequestBody Sendinfo model) {
        contractService.saveOrUpdateSend(model);
        return HttpResult.ok();
    }
 	/**
    * 修改
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('order:contract:invoice')")
    @RequestMapping("/editInvoic")
    public HttpResult editInvoic(@RequestBody Invoic model) {
        contractService.saveOrUpdateInvoic(model);
        return HttpResult.ok();
    }
    /**
    * 查询详情
    *
    * @param id
    * @return
    */
    @PreAuthorize("hasAuthority('order:contract:view')")
    @RequestMapping("/getSend")
    public HttpResult getSend(@RequestBody Contract contract) {
    	Sendinfo model = contractService.findSendInfo(contract);
       
        return HttpResult.ok(model);
    }

    @PreAuthorize("hasAuthority('order:contract:view')")
    @RequestMapping("/getInvoic")
    public HttpResult getInvoic(@RequestBody Contract contract) {
    	Invoic model = contractService.findInvoice(contract);
       
        return HttpResult.ok(model);
    }
     
}

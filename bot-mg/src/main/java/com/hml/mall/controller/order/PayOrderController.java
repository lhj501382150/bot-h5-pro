package com.hml.mall.controller.order;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.hml.core.http.HttpResult;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.order.PayOrder;
import com.hml.mall.iface.order.IPayOrderService;

 


/**
*  前端控制器
* @author hml
* @since 2021-07-10
*/


@RestController
@RequestMapping("/payOrder" )
public class PayOrderController {

    @Autowired
    private IPayOrderService  payOrderService;

 	/**
    * 修改
    *
    * @param model
    * @return
 	 * @throws Exception 
    */
    @PreAuthorize("hasAuthority('order:payorder:edit')")
    @RequestMapping("/edit")
    public HttpResult edit(@RequestBody PayOrder model) throws Exception {
        payOrderService.updateOrder(model);
        return HttpResult.ok();
    }
	/**
    * 删除
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('order:payorder:del')")
    @RequestMapping("/delete")
    public HttpResult delete(@RequestBody PayOrder model) {
        payOrderService.removeById(model);
        return HttpResult.ok();
    }
   
    /**
    * 查询详情
    *
    * @param id
    * @return
    */
    @PreAuthorize("hasAuthority('order:payorder:view')")
    @RequestMapping("/getById")
    public HttpResult getById(Integer id) {
     PayOrder model = payOrderService.getById(id);
        // todo 再包装一层
        return HttpResult.ok(model);
    }

    /**
     * 列表查询（非分页）
     *
     * @return
     * @throws Exception 
     */
     @PreAuthorize("hasAuthority('order:payorder:view')")
     @RequestMapping("/balance")
     public HttpResult balance(@RequestBody PayOrder model) throws Exception {
        JSONObject ret = payOrderService.queryOrderStatus(model);
         return HttpResult.ok(ret);
     }

    /**
    * 列表查询（非分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('order:payorder:view')")
    @RequestMapping("/list")
    public HttpResult list() {
        PayOrder model = new  PayOrder();
        List<PayOrder> list =  payOrderService.list(model);
        // todo 再包装一层
        return HttpResult.ok(list);
    }


    /**
    * 列表查询（分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('order:payorder:view')")
    @RequestMapping("/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {

        PageResult page = payOrderService.findPage(pageRequest);
        // todo 再包装一层
        return HttpResult.ok(page);
    }
}

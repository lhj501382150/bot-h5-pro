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
import com.hml.mall.entity.order.Order;
import com.hml.mall.iface.order.IOrderService;
import com.hml.mall.iface.user.IUserService;


/**
*  前端控制器
* @author hml
* @since 2021-04-18
*/


@RestController
@RequestMapping("/order" )
public class OrderController {

    @Autowired
    private IOrderService  orderService;
    
    @Autowired
    private IUserService userService;

    /**
    * 保存
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('order:order:add')")
    @RequestMapping("/save")
    public HttpResult save(@RequestBody Order model) {
        orderService.save(model);
        return HttpResult.ok();
    }
 	/**
    * 修改
    *
    * @param model
    * @return
    */
    @RequestMapping("/edit")
    public HttpResult edit(@RequestBody Order model) {
        orderService.updateById(model);
        return HttpResult.ok();
    }
	/**
    * 删除
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('order:order:del')")
    @RequestMapping("/delete")
    public HttpResult delete(@RequestBody Order model) {
        orderService.removeById(model);
        return HttpResult.ok();
    }
    /**
    * 查询详情
    *
    * @param id
    * @return
    */
    @PreAuthorize("hasAuthority('order:order:view')")
    @RequestMapping("/getById")
    public HttpResult getById(Integer id) {
     Order model = orderService.getById(id);
        // todo 再包装一层
        return HttpResult.ok(model);
    }


    /**
    * 列表查询（非分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('order:order:view')")
    @RequestMapping("/list")
    public HttpResult list() {
        Order model = new  Order();
        List<Order> list =  orderService.list(model);
        // todo 再包装一层
        return HttpResult.ok(list);
    }


    /**
    * 列表查询（分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('order:order:view')")
    @RequestMapping("/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {

        PageResult page = orderService.findPage(pageRequest);
        // todo 再包装一层
        return HttpResult.ok(page);
    }
    
    
    @PreAuthorize("hasAuthority('operations:result:view')")
    @RequestMapping("/findCount")
    public HttpResult findCount(@RequestBody PageRequest pageRequest) {
    	
    	PageResult page = orderService.findCount(pageRequest);
    	return HttpResult.ok(page);
    }
    
    @PreAuthorize("hasAuthority('operations:levelResult:view')")
    @RequestMapping("/findLevelCount")
    public HttpResult findLevelCount(@RequestBody PageRequest pageRequest) throws Exception {
    	
    	PageResult page = userService.findUserLevelCount(pageRequest);
    	return HttpResult.ok(page);
    }
    
    @PreAuthorize("hasAuthority('invite:inviteCount:view')")
    @RequestMapping("/findFYCount")
    public HttpResult findFYCount(@RequestBody PageRequest pageRequest) {
    	
    	PageResult page = orderService.findFYCount(pageRequest);
    	// todo 再包装一层
    	return HttpResult.ok(page);
    }

}

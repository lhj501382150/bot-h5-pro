package com.hml.mall.controller.user;




import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hml.core.http.HttpResult;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.user.UserAddress;
import com.hml.mall.iface.user.IUserAddressService;


/**
*  前端控制器
* @author hml
* @since 2021-04-11
*/


@RestController
@RequestMapping("/userAddress" )
public class UserAddressController {

    @Autowired
    private IUserAddressService  userAddressService;

    /**
    * 保存
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('firm:address:add')")
    @RequestMapping("/save")
    public HttpResult save(@RequestBody UserAddress model) {
        userAddressService.save(model);
        return HttpResult.ok();
    }
 	/**
    * 修改
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('firm:address:edit')")
    @RequestMapping("/edit")
    public HttpResult edit(@RequestBody UserAddress model) {
        userAddressService.updateById(model);
        return HttpResult.ok();
    }
    /**
     * 修改
     *
     * @param model
     * @return
     */
    @PreAuthorize("hasAuthority('firm:address:del')")
    @RequestMapping("/delete")
    public HttpResult delete(@RequestBody UserAddress model) {
    	userAddressService.removeById(model.getRecvno());
    	return HttpResult.ok();
    }
    /**
    * 查询详情
    *
    * @param id
    * @return
    */
    @PreAuthorize("hasAuthority('firm:address:view')")
    @RequestMapping("/getById")
    public HttpResult getById(Integer id) {
     UserAddress model = userAddressService.getById(id);
        // todo 再包装一层
        return HttpResult.ok(model);
    }


    /**
    * 列表查询（非分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('firm:address:view')")
    @RequestMapping("/list")
    public HttpResult list(@RequestBody UserAddress model) {
        List<UserAddress> list =  userAddressService.list(model);
        // todo 再包装一层
        return HttpResult.ok(list);
    }


    /**
    * 列表查询（分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('firm:address:view')")
    @RequestMapping("/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {

        PageResult page = userAddressService.findPage(pageRequest);
        // todo 再包装一层
        return HttpResult.ok(page);
    }

}

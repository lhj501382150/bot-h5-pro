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
import com.hml.mall.entity.trade.WareJjHandorder;
import com.hml.mall.entity.user.User;
import com.hml.mall.entity.user.UserDigitart;
import com.hml.mall.iface.trade.IWareJjHandorderService;
import com.hml.mall.iface.user.IUserDigitartService;
import com.hml.mall.iface.user.IUserService;
import com.hml.utils.StringUtils;


/**
*  前端控制器
* @author hml
* @since 2021-09-30
*/


@RestController
@RequestMapping("/wareHandorder" )
public class WareJjHandorderController {

    @Autowired
    private IWareJjHandorderService  wareJjHandorderService;

    @Autowired
    private IUserDigitartService userDigitartService;
    @Autowired
    private IUserService userService;
    /**
    * 保存
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('trade:warehandorder:add')")
    @RequestMapping("/save")
    public HttpResult save(@RequestBody WareJjHandorder model) {
    	if(!checkArt(model)){
    		return HttpResult.error("请输入正确资产编号");
    	}
    	if(!checkUser(model)){
    		return HttpResult.error("请输入正确拍卖委托人");
    	}
    	model.setStatus("1");
    	model.setReqstatus("0");
        wareJjHandorderService.save(model);
        return HttpResult.ok();
    }
 	/**
    * 修改
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('trade:warehandorder:edit')")
    @RequestMapping("/edit")
    public HttpResult edit(@RequestBody WareJjHandorder model) {
    	if(!checkArt(model)){
    		return HttpResult.error("请输入正确资产编号");
    	}
    	if(!checkUser(model)){
    		return HttpResult.error("请输入正确拍卖委托人");
    	}
    	WareJjHandorder item = wareJjHandorderService.getById(model.getHid());
    	if("1".equals(item.getReqstatus())){
    		return HttpResult.error("数据已审核，暂不能修改");
    	}
    	if("2".equals(item.getReqstatus())){
    		return HttpResult.error("数据已驳回，暂不能修改");
    	}
        wareJjHandorderService.updateById(model);
        return HttpResult.ok();
    }
	/**
    * 删除
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('trade:warehandorder:del')")
    @RequestMapping("/delete")
    public HttpResult delete(@RequestBody WareJjHandorder model) {
    	WareJjHandorder item = wareJjHandorderService.getById(model.getHid());
    	if("1".equals(item.getReqstatus())){
    		return HttpResult.error("数据已审核，暂不能删除");
    	}
    	if("2".equals(item.getReqstatus())){
    		return HttpResult.error("数据已驳回，暂不能删除");
    	}
        wareJjHandorderService.removeById(model);
        return HttpResult.ok();
    }
    @PreAuthorize("hasAuthority('trade:warehandorder:audit')")
    @RequestMapping("/audit")
    public HttpResult audit(@RequestBody WareJjHandorder model) throws Exception {
    	WareJjHandorder item = wareJjHandorderService.getById(model.getHid());
    	if(!"0".equals(item.getReqstatus())){
    		return HttpResult.error("数据已处理，请勿重复操作");
    	}
        wareJjHandorderService.audit(model);
        return HttpResult.ok();
    }
    @PreAuthorize("hasAuthority('trade:warehandorder:refuse')")
    @RequestMapping("/refuse")
    public HttpResult refuse(@RequestBody WareJjHandorder model) throws Exception {
    	WareJjHandorder item = wareJjHandorderService.getById(model.getHid());
    	if(!"0".equals(item.getReqstatus())){
    		return HttpResult.error("数据已处理，请勿重复操作");
    	}
        wareJjHandorderService.reject(model);
        return HttpResult.ok();
    }
    /**
    * 查询详情
    *
    * @param id
    * @return
    */
    @PreAuthorize("hasAuthority('trade:warehandorder:view')")
    @RequestMapping("/getById")
    public HttpResult getById(Integer id) {
     WareJjHandorder model = wareJjHandorderService.getById(id);
        // todo 再包装一层
        return HttpResult.ok(model);
    }


    /**
    * 列表查询（非分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('trade:warehandorder:view')")
    @RequestMapping("/list")
    public HttpResult list() {
        WareJjHandorder model = new  WareJjHandorder();
        List<WareJjHandorder> list =  wareJjHandorderService.list(model);
        // todo 再包装一层
        return HttpResult.ok(list);
    }


    /**
    * 列表查询（分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('trade:warehandorder:view')")
    @RequestMapping("/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {

        PageResult page = wareJjHandorderService.findPage(pageRequest);
        // todo 再包装一层
        return HttpResult.ok(page);
    }
//  true 存在 false 不存在
    private boolean checkArt(WareJjHandorder model){
    	if(model.getArtid() == null){
    		return false;
    	}
    	UserDigitart item = userDigitartService.getById(model.getArtid());
    	return item != null;
    }
    
    private boolean checkUser(WareJjHandorder model){
    	if(StringUtils.isBlank(model.getSuserno())){
    		return false;
    	}
    	User item = userService.getById(model.getSuserno());
    	return item != null;
    }
}

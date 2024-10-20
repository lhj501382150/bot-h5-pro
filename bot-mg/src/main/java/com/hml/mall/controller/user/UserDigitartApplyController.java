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
import com.hml.mall.entity.user.UserDigitartApply;
import com.hml.mall.iface.user.IUserDigitartApplyService;


/**
*  前端控制器
* @author hml
* @since 2022-05-25
*/


@RestController
@RequestMapping("/userDigitartApply" )
public class UserDigitartApplyController {

    @Autowired
    private IUserDigitartApplyService  userDigitartApplyService;

    /**
    * 保存
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('trade:digitartApply:add')")
    @RequestMapping("/save")
    public HttpResult save(@RequestBody UserDigitartApply model) {
        userDigitartApplyService.save(model);
        return HttpResult.ok();
    }
 	/**
    * 修改
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('trade:digitartApply:edit')")
    @RequestMapping("/edit")
    public HttpResult edit(@RequestBody UserDigitartApply model) {
    	UserDigitartApply item = userDigitartApplyService.getById(model.getArtid());
    	if(!"1".equals(item.getReqstatus())){
    		return HttpResult.error("该数据已处理，暂不能修改！");
    	}
        userDigitartApplyService.updateById(model);
        return HttpResult.ok();
    }
	/**
    * 删除
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('trade:digitartApply:del')")
    @RequestMapping("/delete")
    public HttpResult delete(@RequestBody UserDigitartApply model) {
    	UserDigitartApply item = userDigitartApplyService.getById(model.getArtid());
    	if(!"1".equals(item.getReqstatus())){
    		return HttpResult.error("该数据已处理，暂不能删除！");
    	}
        userDigitartApplyService.removeById(model);
        return HttpResult.ok();
    }
    /**
     * 审核
     *
     * @param model
     * @return
     * @throws Exception 
     */
     @PreAuthorize("hasAuthority('trade:digitartApply:audit')")
     @RequestMapping("audit")
     public HttpResult audit(@RequestBody UserDigitartApply model) throws Exception {
         userDigitartApplyService.audit(model);
         return HttpResult.ok();
     }
     /**
      * 删除
      *
      * @param model
      * @return
     * @throws Exception 
      */
      @PreAuthorize("hasAuthority('trade:digitartApply:reject')")
      @RequestMapping("/reject")
      public HttpResult reject(@RequestBody UserDigitartApply model) throws Exception {
          userDigitartApplyService.refuse(model);
          return HttpResult.ok();
      }
    /**
    * 查询详情
    *
    * @param id
    * @return
    */
    @PreAuthorize("hasAuthority('trade:digitartApply:view')")
    @RequestMapping("/getById")
    public HttpResult getById(Integer id) {
     UserDigitartApply model = userDigitartApplyService.getById(id);
        // todo 再包装一层
        return HttpResult.ok(model);
    }


    /**
    * 列表查询（非分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('trade:digitartApply:view')")
    @RequestMapping("/list")
    public HttpResult list() {
        UserDigitartApply model = new  UserDigitartApply();
        List<UserDigitartApply> list =  userDigitartApplyService.list(model);
        // todo 再包装一层
        return HttpResult.ok(list);
    }


    /**
    * 列表查询（分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('trade:digitartApply:view')")
    @RequestMapping("/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {

        PageResult page = userDigitartApplyService.findPage(pageRequest);
        // todo 再包装一层
        return HttpResult.ok(page);
    }

}

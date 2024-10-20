package com.hml.mall.controller.trade;


import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hml.core.http.HttpResult;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.trade.WareApply;
import com.hml.mall.entity.trade.WareAttrApply;
import com.hml.mall.iface.trade.IWareApplyService;


/**
*  前端控制器
* @author hml
* @since 2021-05-18
*/


@RestController
@RequestMapping("/wareApply" )
public class WareApplyController {

    @Autowired
    private IWareApplyService  wareApplyService;

    /**
    * 保存
    *
    * @param model
    * @return
     * @throws Exception 
    */
    @PreAuthorize("hasAuthority('trade:wareapply:add')")
    @RequestMapping("/save")
    public HttpResult save(@RequestBody WareApply model) throws Exception {
        wareApplyService.saveWare(model);
        return HttpResult.ok();
    }
 	/**
    * 修改
    *
    * @param model
    * @return
 	 * @throws Exception 
    */
    @PreAuthorize("hasAuthority('trade:wareapply:edit')")
    @RequestMapping("/edit")
    public HttpResult edit(@RequestBody WareApply model) throws Exception {
        wareApplyService.updateWare(model);
        return HttpResult.ok();
    }
	/**
    * 删除
    *
    * @param model
    * @return
	 * @throws Exception 
    */
    @PreAuthorize("hasAuthority('trade:wareapply:del')")
    @RequestMapping("/delete")
    public HttpResult delete(@RequestBody WareApply model) throws Exception {
        wareApplyService.delWare(model);
        return HttpResult.ok();
    }
    /**
    * 查询详情
    *
    * @param id
    * @return
    */
    @PreAuthorize("hasAuthority('trade:wareapply:view')")
    @RequestMapping("/getById")
    public HttpResult getById(@RequestBody WareApply ware) {
     WareApply model = wareApplyService.getById(ware.getWaterid());
        // todo 再包装一层
        return HttpResult.ok(model);
    }


    /**
    * 列表查询（非分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('trade:wareapply:view')")
    @RequestMapping("/list")
    public HttpResult list() {
        WareApply model = new  WareApply();
        List<WareApply> list =  wareApplyService.list(model);
        // todo 再包装一层
        return HttpResult.ok(list);
    }


    /**
    * 列表查询（分页）
    * 查询待提交 和 已驳回的
    * @return
    */
    @PreAuthorize("hasAuthority('trade:wareapply:view')")
    @RequestMapping("/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {
    	Integer[] arr = new Integer[]{0,3};
    	pageRequest.getParams().put("STATUS@IN",Arrays.asList(arr));
        PageResult page = wareApplyService.findPage(pageRequest);
        // todo 再包装一层
        return HttpResult.ok(page);
    }

    /*
     * 查询待审核列表
     */
    @PreAuthorize("hasAuthority('trade:wareapply:auditlist')")
    @RequestMapping("/findAuditPage")
    public HttpResult findAuditPage(@RequestBody PageRequest pageRequest) {
    	pageRequest.getParams().put("STATUS",1);
        PageResult page = wareApplyService.findPage(pageRequest);
        // todo 再包装一层
        return HttpResult.ok(page);
    }
    
    /*
     * 查询已审核列表
     */
    @PreAuthorize("hasAuthority('trade:wareapply:list')")
    @RequestMapping("/findListPage")
    public HttpResult findListPage(@RequestBody PageRequest pageRequest) {
    	pageRequest.getParams().put("STATUS",2);
        PageResult page = wareApplyService.findPage(pageRequest);
        // todo 再包装一层
        return HttpResult.ok(page);
    }
    
    /**
     * 修改
     *
     * @param model
     * @return
  	 * @throws Exception 
     */
     @PreAuthorize("hasAuthority('trade:wareapply:apply')")
     @RequestMapping("/apply")
     public HttpResult apply(@RequestBody WareApply model) throws Exception {
         wareApplyService.submitWareApply(model);
         return HttpResult.ok();
     }
     
     /**
      * 修改
      *
      * @param model
      * @return
   	 * @throws Exception 
      */
      @PreAuthorize("hasAuthority('trade:wareapply:audit')")
      @RequestMapping("/audit")
      public HttpResult audit(@RequestBody WareApply model) throws Exception {
    	  model.setStatus(2);
          wareApplyService.auditWareApply(model);
          return HttpResult.ok();
      }
      
      /**
       * 修改
       *
       * @param model
       * @return
    	 * @throws Exception 
       */
       @PreAuthorize("hasAuthority('trade:wareapply:refuse')")
       @RequestMapping("/refuse")
       public HttpResult refuse(@RequestBody WareApply model) throws Exception {
    	   model.setStatus(3);
           wareApplyService.auditWareApply(model);
           return HttpResult.ok();
       }
       
       /**
        * 查询属性
        *
        * @return
        * @throws Exception 
        */
        @RequestMapping("/attr")
        public HttpResult attr(@RequestBody WareApply ware) throws Exception {
            List<WareAttrApply> list =  wareApplyService.findWareAttr(ware);
            return HttpResult.ok(list);
        }
}

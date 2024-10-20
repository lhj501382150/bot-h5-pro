package com.hml.mall.controller.trade;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hml.core.http.HttpResult;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.trade.Wareclass;
import com.hml.mall.entity.trade.WareclassAttrcol;
import com.hml.mall.iface.trade.IWareclassAttrcolService;
import com.hml.mall.iface.trade.IWareclassService;


/**
*  前端控制器
* @author hml
* @since 2021-04-17
*/


@RestController
@RequestMapping("/wareclass" )
public class WareclassController {

    @Autowired
    private IWareclassService  wareclassService;
    
    @Autowired
    private IWareclassAttrcolService wareclassAttrcolService;

    /**
    * 保存
    *
    * @param model
    * @return
     * @throws Exception 
    */
    @PreAuthorize("hasAuthority('trade:wareclass:add')")
    @RequestMapping("/save")
    public HttpResult save(@RequestBody Wareclass model) throws Exception {
        wareclassService.saveWareclass(model);
        return HttpResult.ok();
    }
 	/**
    * 修改
    *
    * @param model
    * @return
 	 * @throws Exception 
    */
    @PreAuthorize("hasAuthority('trade:wareclass:edit')")
    @RequestMapping("/edit")
    public HttpResult edit(@RequestBody Wareclass model) throws Exception {
        wareclassService.updateWareClass(model);
        return HttpResult.ok();
    }
	/**
    * 删除
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('trade:wareclass:del')")
    @RequestMapping("/delete")
    public HttpResult delete(@RequestBody Wareclass model) {
        wareclassService.removeById(model.getCno());
        return HttpResult.ok();
    }
    /**
    * 查询详情
    *
    * @param id
    * @return
    */
    @PreAuthorize("hasAuthority('trade:wareclass:view')")
    @RequestMapping("/getById")
    public HttpResult getById(Integer id) {
     Wareclass model = wareclassService.getById(id);
        // todo 再包装一层
        return HttpResult.ok(model);
    }


    /**
    * 列表查询（非分页）
    *
    * @return
    */
    @RequestMapping("/list")
    public HttpResult list(@RequestBody Wareclass model) {
        List<Wareclass> list =  wareclassService.list(model);
        // todo 再包装一层
        return HttpResult.ok(list);
    }


    /**
    * 列表查询（分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('trade:wareclass:view')")
    @RequestMapping("/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {

        PageResult page = wareclassService.findPage(pageRequest);
        // todo 再包装一层
        return HttpResult.ok(page);
    }

    /**
     * 列表查询（非分页）
     *
     * @return
     */
     @RequestMapping("/attrlist")
     public HttpResult attrlist(@RequestBody Wareclass model) {
    	 QueryWrapper<WareclassAttrcol> qw = new QueryWrapper<WareclassAttrcol>();
    	 qw.eq("CNO", model.getCno());
    	 List<WareclassAttrcol> list = wareclassAttrcolService.list(qw);
         // todo 再包装一层
         return HttpResult.ok(list);
     }
}

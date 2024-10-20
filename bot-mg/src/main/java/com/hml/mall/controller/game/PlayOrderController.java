package com.hml.mall.controller.game;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hml.core.http.HttpResult;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.game.PlayOrder;
import com.hml.mall.iface.game.IPlayOrderService;


/**
*  前端控制器
* @author hml
* @since 2021-07-11
*/


@RestController
@RequestMapping("/playOrder" )
public class PlayOrderController {

    @Autowired
    private IPlayOrderService  playOrderService;

    /**
    * 保存
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('order:playorder:add')")
    @RequestMapping("/save")
    public HttpResult save(@RequestBody PlayOrder model) {
        playOrderService.save(model);
        return HttpResult.ok();
    }
 	/**
    * 修改
    *
    * @param model
    * @return
 	 * @throws Exception 
    */
    @PreAuthorize("hasAuthority('order:playorder:edit')")
    @RequestMapping("/edit")
    public HttpResult edit(@RequestBody PlayOrder model) throws Exception {
    	model.setOptType("1");
        playOrderService.sendPlayOrder(model);
        return HttpResult.ok();
    }
	/**
    * 删除
    *
    * @param model
    * @return
	 * @throws Exception 
    */
    @PreAuthorize("hasAuthority('order:playorder:del')")
    @RequestMapping("/delete")
    public HttpResult delete(@RequestBody PlayOrder model) throws Exception {
    	model.setOptType("2");
    	playOrderService.sendPlayOrder(model);
        return HttpResult.ok();
    }
    /**
    * 查询详情
    *
    * @param id
    * @return
    */
    @PreAuthorize("hasAuthority('order:playorder:view')")
    @RequestMapping("/getById")
    public HttpResult getById(Integer id) {
     PlayOrder model = playOrderService.getById(id);
        // todo 再包装一层
        return HttpResult.ok(model);
    }


    /**
    * 列表查询（非分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('order:playorder:view')")
    @RequestMapping("/list")
    public HttpResult list() {
        PlayOrder model = new  PlayOrder();
        List<PlayOrder> list =  playOrderService.list(model);
        // todo 再包装一层
        return HttpResult.ok(list);
    }


    /**
    * 列表查询（分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('order:playorder:view')")
    @RequestMapping("/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {

        PageResult page = playOrderService.findPage(pageRequest);
        // todo 再包装一层
        return HttpResult.ok(page);
    }

}

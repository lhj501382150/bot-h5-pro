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
import com.hml.mall.entity.game.Rewar;
import com.hml.mall.iface.game.IRewarService;


/**
*  前端控制器
* @author hml
* @since 2021-06-09
*/


@RestController
@RequestMapping("/rewar" )
public class RewarController {

    @Autowired
    private IRewarService  rewarService;

    /**
    * 保存
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('gm:rewar:add')")
    @RequestMapping("/save")
    public HttpResult save(@RequestBody Rewar model) {
        rewarService.save(model);
        return HttpResult.ok();
    }
 	/**
    * 修改
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('gm:rewar:edit')")
    @RequestMapping("/edit")
    public HttpResult edit(@RequestBody Rewar model) {
        rewarService.updateById(model);
        return HttpResult.ok();
    }
	/**
    * 删除
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('gm:rewar:del')")
    @RequestMapping("/delete")
    public HttpResult delete(@RequestBody Rewar model) {
        rewarService.removeById(model);
        return HttpResult.ok();
    }
    /**
    * 查询详情
    *
    * @param id
    * @return
    */
    @PreAuthorize("hasAuthority('gm:rewar:view')")
    @RequestMapping("/getById")
    public HttpResult getById(Integer id) {
     Rewar model = rewarService.getById(id);
        // todo 再包装一层
        return HttpResult.ok(model);
    }


    /**
    * 列表查询（非分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('gm:rewar:view')")
    @RequestMapping("/list")
    public HttpResult list() {
        Rewar model = new  Rewar();
        List<Rewar> list =  rewarService.list(model);
        // todo 再包装一层
        return HttpResult.ok(list);
    }


    /**
    * 列表查询（分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('gm:rewar:view')")
    @RequestMapping("/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {

        PageResult page = rewarService.findPage(pageRequest);
        // todo 再包装一层
        return HttpResult.ok(page);
    }

}

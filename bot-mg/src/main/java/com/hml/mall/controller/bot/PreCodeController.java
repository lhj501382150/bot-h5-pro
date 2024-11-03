package com.hml.mall.controller.bot;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hml.core.http.HttpResult;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.bot.PreCode;
import com.hml.mall.iface.bot.IPreCodeService;
import com.hml.mall.util.SecurityUtils;


/**
*  前端控制器
* @author hml
* @since 2024-11-03
*/


@RestController
@RequestMapping("/preCode" )
public class PreCodeController {

    @Autowired
    private IPreCodeService  preCodeService;

    /**
    * 保存
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('operations:preCode:add')")
    @RequestMapping("/save")
    public HttpResult save(@RequestBody PreCode model) {
    	model.setCreateTime(LocalDateTime.now());
    	model.setCreateUser(SecurityUtils.getUsername());
        preCodeService.save(model);
        return HttpResult.ok();
    }
 	/**
    * 修改
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('operations:preCode:edit')")
    @RequestMapping("/edit")
    public HttpResult edit(@RequestBody PreCode model) {
    	model.setUpdateTime(LocalDateTime.now());
    	model.setUpdateUser(SecurityUtils.getUsername());
        preCodeService.updateById(model);
        return HttpResult.ok();
    }
	/**
    * 删除
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('operations:preCode:del')")
    @RequestMapping("/delete")
    public HttpResult delete(@RequestBody PreCode model) {
        preCodeService.removeById(model);
        return HttpResult.ok();
    }
    /**
    * 查询详情
    *
    * @param id
    * @return
    */
    @PreAuthorize("hasAuthority('operations:preCode:view')")
    @RequestMapping("/getById")
    public HttpResult getById(Integer id) {
    	PreCode model = preCodeService.getById(id);
         
        return HttpResult.ok(model);
    }


    /**
    * 列表查询（非分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('operations:preCode:view')")
    @RequestMapping("/list")
    public HttpResult list() {
        PreCode model = new  PreCode();
        List<PreCode> list =  preCodeService.list(model);
        // todo 再包装一层
        return HttpResult.ok(list);
    }


    /**
    * 列表查询（分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('operations:preCode:view')")
    @RequestMapping("/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {
    	pageRequest.getParams().put("id@desc", "a");
        PageResult page = preCodeService.findPage(pageRequest);
        
        return HttpResult.ok(page);
    }

}

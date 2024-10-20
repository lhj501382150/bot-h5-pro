package com.hml.mall.controller.notice;


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
import com.hml.mall.entity.notice.Mess;
import com.hml.mall.iface.notice.IMessService;


/**
*  前端控制器
* @author hml
* @since 2021-04-20
*/


@RestController
@RequestMapping("/mess" )
public class MessController {

    @Autowired
    private IMessService  messService;

    /**
    * 保存
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('info:mess:add')")
    @RequestMapping("/save")
    public HttpResult save(@RequestBody Mess model) {
        messService.save(model);
        return HttpResult.ok();
    }
 	/**
    * 修改
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('info:mess:edit')")
    @RequestMapping("/edit")
    public HttpResult edit(@RequestBody Mess model) {
        messService.updateById(model);
        return HttpResult.ok();
    }
	/**
    * 删除
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('info:mess:del')")
    @RequestMapping("/delete")
    public HttpResult delete(@RequestBody Mess model) {
        messService.removeById(model);
        return HttpResult.ok();
    }
    /**
    * 查询详情
    *
    * @param id
    * @return
    */
    @PreAuthorize("hasAuthority('info:mess:view')")
    @RequestMapping("/getById")
    public HttpResult getById(Integer id) {
     Mess model = messService.getById(id);
        // todo 再包装一层
        return HttpResult.ok(model);
    }


    /**
    * 列表查询（非分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('info:mess:view')")
    @RequestMapping("/list")
    public HttpResult list() {
       QueryWrapper<Mess> qw = new QueryWrapper<Mess>();
        List<Mess> list =  messService.list(qw);
        // todo 再包装一层
        return HttpResult.ok(list);
    }


    /**
    * 列表查询（分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('info:mess:view')")
    @RequestMapping("/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {

        PageResult page = messService.findPage(pageRequest);
        // todo 再包装一层
        return HttpResult.ok(page);
    }

}

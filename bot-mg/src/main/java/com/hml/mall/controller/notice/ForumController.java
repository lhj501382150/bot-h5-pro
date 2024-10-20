package com.hml.mall.controller.notice;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hml.core.http.HttpResult;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.notice.Forum;
import com.hml.mall.iface.notice.IForumService;


/**
*  前端控制器
* @author hml
* @since 2021-09-06
*/


@RestController
@RequestMapping("/forum" )
public class ForumController {

    @Autowired
    private IForumService  forumService;

    /**
    * 保存
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('info:forum:add')")
    @RequestMapping("/save")
    public HttpResult save(@RequestBody Forum model) {
        forumService.save(model);
        return HttpResult.ok();
    }
 	/**
    * 修改
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('info:forum:edit')")
    @RequestMapping("/edit")
    public HttpResult edit(@RequestBody Forum model) {
        forumService.updateById(model);
        return HttpResult.ok();
    }
	/**
    * 删除
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('info:forum:del')")
    @RequestMapping("/delete")
    public HttpResult delete(@RequestBody Forum model) {
        forumService.removeById(model);
        return HttpResult.ok();
    }
    /**
    * 查询详情
    *
    * @param id
    * @return
    */
    @PreAuthorize("hasAuthority('info:forum:view')")
    @RequestMapping("/getById")
    public HttpResult getById(Integer id) {
     Forum model = forumService.getById(id);
        // todo 再包装一层
        return HttpResult.ok(model);
    }


    /**
    * 列表查询（非分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('info:forum:view')")
    @RequestMapping("/list")
    public HttpResult list() {
        Forum model = new  Forum();
        List<Forum> list =  forumService.list(model);
        // todo 再包装一层
        return HttpResult.ok(list);
    }


    /**
    * 列表查询（分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('info:forum:view')")
    @RequestMapping("/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {

        PageResult page = forumService.findPage(pageRequest);
        // todo 再包装一层
        return HttpResult.ok(page);
    }

}

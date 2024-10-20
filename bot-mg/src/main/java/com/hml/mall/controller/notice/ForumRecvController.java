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
import com.hml.mall.entity.notice.ForumRecv;
import com.hml.mall.iface.notice.IForumRecvService;


/**
*  前端控制器
* @author hml
* @since 2021-09-06
*/


@RestController
@RequestMapping("/forumRecv" )
public class ForumRecvController {

    @Autowired
    private IForumRecvService  forumRecvService;

    /**
    * 保存
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('info:forumrecv:add')")
    @RequestMapping("/save")
    public HttpResult save(@RequestBody ForumRecv model) {
        forumRecvService.save(model);
        return HttpResult.ok();
    }
 	/**
    * 修改
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('info:forumrecv:edit')")
    @RequestMapping("/edit")
    public HttpResult edit(@RequestBody ForumRecv model) {
        forumRecvService.updateById(model);
        return HttpResult.ok();
    }
	/**
    * 删除
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('info:forumrecv:del')")
    @RequestMapping("/delete")
    public HttpResult delete(@RequestBody ForumRecv model) {
        forumRecvService.removeById(model);
        return HttpResult.ok();
    }
    /**
    * 查询详情
    *
    * @param id
    * @return
    */
    @PreAuthorize("hasAuthority('info:forumrecv:view')")
    @RequestMapping("/getById")
    public HttpResult getById(Integer id) {
     ForumRecv model = forumRecvService.getById(id);
        // todo 再包装一层
        return HttpResult.ok(model);
    }


    /**
    * 列表查询（非分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('info:forumrecv:view')")
    @RequestMapping("/list")
    public HttpResult list() {
        ForumRecv model = new  ForumRecv();
        List<ForumRecv> list =  forumRecvService.list(model);
        // todo 再包装一层
        return HttpResult.ok(list);
    }


    /**
    * 列表查询（分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('info:forumrecv:view')")
    @RequestMapping("/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {

        PageResult page = forumRecvService.findPage(pageRequest);
        // todo 再包装一层
        return HttpResult.ok(page);
    }

}

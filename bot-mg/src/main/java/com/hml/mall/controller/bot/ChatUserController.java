package com.hml.mall.controller.bot;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hml.core.http.HttpResult;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.bot.ChatUser;
import com.hml.mall.iface.bot.IChatUserService;


/**
*  前端控制器
* @author hml
* @since 2023-12-08
*/


@RestController
@RequestMapping("/chatUser" )
public class ChatUserController {

    @Autowired
    private IChatUserService  chatUserService;

    /**
    * 保存
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('sys:chatUser:add')")
    @RequestMapping("/save")
    public HttpResult save(@RequestBody ChatUser model) {
        chatUserService.save(model);
        return HttpResult.ok();
    }
 	/**
    * 修改
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('sys:chatUser:edit')")
    @RequestMapping("/edit")
    public HttpResult edit(@RequestBody ChatUser model) {
        chatUserService.updateById(model);
        return HttpResult.ok();
    }
	/**
    * 删除
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('sys:chatUser:del')")
    @RequestMapping("/delete")
    public HttpResult delete(@RequestBody ChatUser model) {
        chatUserService.removeById(model);
        return HttpResult.ok();
    }
    /**
    * 查询详情
    *
    * @param id
    * @return
    */
    @PreAuthorize("hasAuthority('sys:chatUser:view')")
    @RequestMapping("/getById")
    public HttpResult getById(Integer id) {
     ChatUser model = chatUserService.getById(id);
        // todo 再包装一层
        return HttpResult.ok(model);
    }


    /**
    * 列表查询（非分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('sys:chatUser:view')")
    @RequestMapping("/list")
    public HttpResult list() {
        ChatUser model = new  ChatUser();
        List<ChatUser> list =  chatUserService.list(model);
        // todo 再包装一层
        return HttpResult.ok(list);
    }


    /**
    * 列表查询（分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('sys:chatUser:view')")
    @RequestMapping("/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {

        PageResult page = chatUserService.findPage(pageRequest);
        // todo 再包装一层
        return HttpResult.ok(page);
    }

}

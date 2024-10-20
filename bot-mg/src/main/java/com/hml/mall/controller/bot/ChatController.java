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
import com.hml.mall.entity.bot.Chat;
import com.hml.mall.iface.bot.IChatService;


/**
*  前端控制器
* @author hml
* @since 2023-12-08
*/


@RestController
@RequestMapping("/chat" )
public class ChatController {

    @Autowired
    private IChatService  chatService;

    /**
    * 保存
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('sys:chat:add')")
    @RequestMapping("/save")
    public HttpResult save(@RequestBody Chat model) {
    	model.setChatid(model.getChatid().trim());
    	model.setStatus("N");
        chatService.save(model);
        return HttpResult.ok();
    }
 	/**
    * 修改
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('sys:chat:edit')")
    @RequestMapping("/edit")
    public HttpResult edit(@RequestBody Chat model) {
        chatService.updateById(model);
        return HttpResult.ok();
    }
    @PreAuthorize("hasAuthority('sys:chat:down')")
    @RequestMapping("/down")
    public HttpResult down(@RequestBody Chat model) {
        chatService.down(model);
        return HttpResult.ok();
    }
    @PreAuthorize("hasAuthority('sys:chat:pause')")
    @RequestMapping("/pause")
    public HttpResult pause(@RequestBody Chat model) {
    	Chat item = chatService.getById(model.getChatid());
    	if(!"Y".equals(item.getStatus())){
    		return HttpResult.error("当前不需要暂停");
    	}
    	item.setStatus("N");
        chatService.editStatus(item);
        return HttpResult.ok();
    }
    @PreAuthorize("hasAuthority('sys:chat:start')")
    @RequestMapping("/restart")
    public HttpResult restart(@RequestBody Chat model) {
    	Chat item = chatService.getById(model.getChatid());
    	if(!"N".equals(item.getStatus())){
    		return HttpResult.error("当前不需要重启");
    	}
    	item.setStatus("Y");
        chatService.editStatus(item);
        return HttpResult.ok();
    }
	/**
    * 删除
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('sys:chat:del')")
    @RequestMapping("/delete")
    public HttpResult delete(@RequestBody Chat model) {
        chatService.removeById(model);
        return HttpResult.ok();
    }
    /**
    * 查询详情
    *
    * @param id
    * @return
    */
    @PreAuthorize("hasAuthority('sys:chat:view')")
    @RequestMapping("/getById")
    public HttpResult getById(Integer id) {
     Chat model = chatService.getById(id);
        // todo 再包装一层
        return HttpResult.ok(model);
    }


    /**
    * 列表查询（非分页）
    *
    * @return
    */
    @RequestMapping("/list")
    public HttpResult list(@RequestBody  Chat model) {
        List<Chat> list =  chatService.list(model);
        return HttpResult.ok(list);
    }


    /**
    * 列表查询（分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('sys:chat:view')")
    @RequestMapping("/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {

        PageResult page = chatService.findPage(pageRequest);
        // todo 再包装一层
        return HttpResult.ok(page);
    }

}

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
import com.hml.mall.entity.bot.Bot;
import com.hml.mall.iface.bot.IBotService;


/**
*  前端控制器
* @author hml
* @since 2023-12-08
*/


@RestController
@RequestMapping("/bot" )
public class BotController {

    @Autowired
    private IBotService  botService;

    /**
    * 保存
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('sys:bot:add')")
    @RequestMapping("/save")
    public HttpResult save(@RequestBody Bot model) {
        botService.save(model);
        return HttpResult.ok();
    }
 	/**
    * 修改
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('sys:bot:edit')")
    @RequestMapping("/edit")
    public HttpResult edit(@RequestBody Bot model) {
        botService.updateById(model);
        return HttpResult.ok();
    }
	/**
    * 删除
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('sys:bot:del')")
    @RequestMapping("/delete")
    public HttpResult delete(@RequestBody Bot model) {
        botService.removeById(model);
        return HttpResult.ok();
    }
    /**
    * 查询详情
    *
    * @param id
    * @return
    */
    @PreAuthorize("hasAuthority('sys:bot:view')")
    @RequestMapping("/getById")
    public HttpResult getById(Integer id) {
     Bot model = botService.getById(id);
        // todo 再包装一层
        return HttpResult.ok(model);
    }


    /**
    * 列表查询（非分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('sys:bot:view')")
    @RequestMapping("/list")
    public HttpResult list() {
        Bot model = new  Bot();
        List<Bot> list =  botService.list(model);
        // todo 再包装一层
        return HttpResult.ok(list);
    }


    /**
    * 列表查询（分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('sys:bot:view')")
    @RequestMapping("/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {

        PageResult page = botService.findPage(pageRequest);
        // todo 再包装一层
        return HttpResult.ok(page);
    }

}

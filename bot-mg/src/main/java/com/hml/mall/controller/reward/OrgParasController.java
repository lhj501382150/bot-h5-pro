package com.hml.mall.controller.reward;


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
import com.hml.mall.entity.reward.OrgParas;
import com.hml.mall.iface.reward.IOrgParasService;


/**
*  前端控制器
* @author hml
* @since 2021-05-26
*/


@RestController
@RequestMapping("/orgParas" )
public class OrgParasController {

    @Autowired
    private IOrgParasService  orgParasService;

    /**
    * 保存
    *
    * @param model
    * @return
     * @throws Exception 
    */
    @PreAuthorize("hasAuthority('reward:orgpara:add')")
    @RequestMapping("/save")
    public HttpResult save(@RequestBody OrgParas model) throws Exception {
    	QueryWrapper<OrgParas> qw = new QueryWrapper<OrgParas>();
    	qw.eq("clevel", model.getClevel());
    	int count = orgParasService.count(qw);
    	if(count > 0) {
    		return HttpResult.error("该层级已设置");
    	}
        orgParasService.save(model);
        return HttpResult.ok();
    }
 	/**
    * 修改
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('reward:orgpara:edit')")
    @RequestMapping("/edit")
    public HttpResult edit(@RequestBody OrgParas model) {
        orgParasService.updateById(model);
        return HttpResult.ok();
    }
	/**
    * 删除
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('reward:orgpara:del')")
    @RequestMapping("/delete")
    public HttpResult delete(@RequestBody OrgParas model) {
        orgParasService.removeById(model);
        return HttpResult.ok();
    }
    /**
    * 查询详情
    *
    * @param id
    * @return
    */
    @PreAuthorize("hasAuthority('reward:orgpara:view')")
    @RequestMapping("/getById")
    public HttpResult getById(Integer id) {
     OrgParas model = orgParasService.getById(id);
        // todo 再包装一层
        return HttpResult.ok(model);
    }


    /**
    * 列表查询（非分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('reward:orgpara:view')")
    @RequestMapping("/list")
    public HttpResult list() {
        OrgParas model = new  OrgParas();
        List<OrgParas> list =  orgParasService.list(model);
        // todo 再包装一层
        return HttpResult.ok(list);
    }


    /**
    * 列表查询（分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('reward:orgpara:view')")
    @RequestMapping("/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {

        PageResult page = orgParasService.findPage(pageRequest);
        // todo 再包装一层
        return HttpResult.ok(page);
    }

}

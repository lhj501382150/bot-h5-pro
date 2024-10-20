package com.hml.mall.controller.game;


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
import com.hml.mall.entity.game.Ranklist;
import com.hml.mall.iface.game.IRanklistService;


/**
*  前端控制器
* @author hml
* @since 2021-06-28
*/


@RestController
@RequestMapping("/ranklist" )
public class RanklistController {

    @Autowired
    private IRanklistService  ranklistService;

    /**
    * 保存
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('gm:ranklist:add')")
    @RequestMapping("/save")
    public HttpResult save(@RequestBody Ranklist model) {
    	QueryWrapper<Ranklist> qw = new QueryWrapper<Ranklist>();
        qw.eq("ltype", model.getLtype());
        qw.eq("cyctype", model.getCyctype());
        qw.eq("seq", model.getSeq());
        
        List<Ranklist> list = ranklistService.list(qw);
        if(list.size() > 0) {
        	return HttpResult.error("数据已存在！");
        }
    	
        ranklistService.save(model);
        return HttpResult.ok();
    }
 	/**
    * 修改
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('gm:ranklist:edit')")
    @RequestMapping("/edit")
    public HttpResult edit(@RequestBody Ranklist model) {
        ranklistService.updateById(model);
        return HttpResult.ok();
    }
	/**
    * 删除
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('gm:ranklist:del')")
    @RequestMapping("/delete")
    public HttpResult delete(@RequestBody Ranklist model) {
//        ranklistService.removeById(model);
        QueryWrapper<Ranklist> qw = new QueryWrapper<Ranklist>();
        qw.eq("ltype", model.getLtype());
        qw.eq("cyctype", model.getCyctype());
        ranklistService.remove(qw);
        return HttpResult.ok();
    }
    /**
    * 查询详情
    *
    * @param id
    * @return
    */
    @PreAuthorize("hasAuthority('gm:ranklist:view')")
    @RequestMapping("/getById")
    public HttpResult getById(Integer id) {
     Ranklist model = ranklistService.getById(id);
        // todo 再包装一层
        return HttpResult.ok(model);
    }


    /**
    * 列表查询（非分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('gm:ranklist:view')")
    @RequestMapping("/list")
    public HttpResult list() {
        Ranklist model = new  Ranklist();
        List<Ranklist> list =  ranklistService.list(model);
        // todo 再包装一层
        return HttpResult.ok(list);
    }


    /**
    * 列表查询（分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('gm:ranklist:view')")
    @RequestMapping("/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {

        PageResult page = ranklistService.findPage(pageRequest);
        // todo 再包装一层
        return HttpResult.ok(page);
    }

}

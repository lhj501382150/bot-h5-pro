package com.hml.mall.controller.money;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hml.core.http.HttpResult;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.money.Subject;
import com.hml.mall.iface.money.ISubjectService;


/**
*  前端控制器
* @author hml
* @since 2021-04-14
*/


@RestController
@RequestMapping("/subject" )
public class SubjectController {

    @Autowired
    private ISubjectService  subjectService;

    /**
    * 保存
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('money:subject:add')")
    @RequestMapping("/save")
    public HttpResult save(@RequestBody Subject model) {
        subjectService.save(model);
        return HttpResult.ok();
    }
 	/**
    * 修改
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('money:subject:edit')")
    @RequestMapping("/edit")
    public HttpResult edit(@RequestBody Subject model) {
        subjectService.updateById(model);
        return HttpResult.ok();
    }
	/**
    * 删除
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('money:subject:del')")
    @RequestMapping("/delete")
    public HttpResult delete(@RequestBody Subject model) {
        subjectService.removeById(model);
        return HttpResult.ok();
    }
    /**
    * 查询详情
    *
    * @param id
    * @return
    */
    @PreAuthorize("hasAuthority('money:subject:view')")
    @RequestMapping("/getById")
    public HttpResult getById(Integer id) {
     Subject model = subjectService.getById(id);
        // todo 再包装一层
        return HttpResult.ok(model);
    }


    /**
    * 列表查询（非分页）
    *
    * @return
    */
    @RequestMapping("/list")
    public HttpResult list() {
        Subject model = new  Subject();
        List<Subject> list =  subjectService.list(model);
        // todo 再包装一层
        return HttpResult.ok(list);
    }


    /**
    * 列表查询（分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('money:subject:view')")
    @RequestMapping("/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {

        PageResult page = subjectService.findPage(pageRequest);
        // todo 再包装一层
        return HttpResult.ok(page);
    }

}

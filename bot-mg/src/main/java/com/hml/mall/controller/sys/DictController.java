package com.hml.mall.controller.sys;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hml.core.http.HttpResult;
import com.hml.mall.entity.sys.Dict;
import com.hml.mall.iface.sys.IDictService;


/**
*  前端控制器
* @author hml
* @since 2021-07-10
*/


@RestController
@RequestMapping("/dict" )
public class DictController {

    @Autowired
    private IDictService  dictService;

    /**
    * 查询详情
    *
    * @param id
    * @return
    */
    @RequestMapping("/getById")
    public HttpResult getById(Integer id) {
     Dict model = dictService.getById(id);
        // todo 再包装一层
        return HttpResult.ok(model);
    }


    /**
    * 列表查询（非分页）
    *
    * @return
    */
    @RequestMapping("/list")
    public HttpResult list(@RequestBody Dict model) {
        List<Dict> list =  dictService.list(model);
       
        return HttpResult.ok(list);
    }
}

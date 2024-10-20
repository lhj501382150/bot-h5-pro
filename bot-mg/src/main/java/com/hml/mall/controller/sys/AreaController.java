package com.hml.mall.controller.sys;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hml.core.http.HttpResult;
import com.hml.mall.entity.sys.Area;
import com.hml.mall.iface.sys.IAreaService;


/**
*  前端控制器
* @author hml
* @since 2021-04-05
*/


@RestController
@RequestMapping("/area" )
public class AreaController {

    @Autowired
    private IAreaService  areaService;

    /**
    * 列表查询（非分页）
    *
    * @return
    */
    @RequestMapping("/list")
    public HttpResult list(@RequestBody Area model) {
        List<Area> list =  areaService.list(model);
        // todo 再包装一层
        return HttpResult.ok(list);
    }
}

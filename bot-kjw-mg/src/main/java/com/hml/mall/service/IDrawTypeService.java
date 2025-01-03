package com.hml.mall.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.DrawType;

/**
* <p>
    *  服务类
    * </p>
*
* @author hml
* @since 2024-12-05
*/


public interface IDrawTypeService extends IService<DrawType> {

   /**
   * 查询
   *
   * @param model
   * @return
   */
   List<DrawType> list(DrawType model);

    /**
    * 分页查询
    *
    * @param model
    * @param pageNum  第几页
    * @param pageSize 每页数量
    * @return
    */
   PageResult findPage(PageRequest pageRequest);

}

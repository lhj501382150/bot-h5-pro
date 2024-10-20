package com.hml.mall.iface.spread;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.spread.GrowParas;

/**
* <p>
    *  服务类
    * </p>
*
* @author hml
* @since 2021-09-06
*/


public interface IGrowParasService extends IService<GrowParas> {

   /**
   * 查询
   *
   * @param model
   * @return
   */
   List<GrowParas> list(GrowParas model);

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

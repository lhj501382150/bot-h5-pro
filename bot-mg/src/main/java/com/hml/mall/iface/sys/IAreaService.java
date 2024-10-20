package com.hml.mall.iface.sys;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hml.mall.entity.sys.Area;

/**
* <p>
    *  服务类
    * </p>
*
* @author hml
* @since 2021-04-05
*/


public interface IAreaService extends IService<Area> {

   /**
   * 查询
   *
   * @param model
   * @return
   */
   List<Area> list(Area model);

    /**
    * 分页查询
    *
    * @param model
    * @param pageNum  第几页
    * @param pageSize 每页数量
    * @return
    */
    IPage<Area> pageList(Area model, Integer pageNum, Integer pageSize);

}

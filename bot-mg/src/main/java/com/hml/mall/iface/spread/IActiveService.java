package com.hml.mall.iface.spread;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.spread.Active;

/**
* <p>
    *  服务类
    * </p>
*
* @author hml
* @since 2022-07-31
*/


public interface IActiveService extends IService<Active> {

   /**
   * 查询
   *
   * @param model
   * @return
   */
   List<Active> list(Active model);

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

package com.hml.mall.iface.sys;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.sys.Dict;

/**
* <p>
    *  服务类
    * </p>
*
* @author hml
* @since 2021-07-10
*/


public interface IDictService extends IService<Dict> {

   /**
   * 查询
   *
   * @param model
   * @return
   */
   List<Dict> list(Dict model);

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

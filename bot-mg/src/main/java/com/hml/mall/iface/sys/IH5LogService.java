package com.hml.mall.iface.sys;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.sys.H5Log;

/**
* <p>
    *  服务类
    * </p>
*
* @author hml
* @since 2024-06-25
*/


public interface IH5LogService extends IService<H5Log> {

   /**
   * 查询
   *
   * @param model
   * @return
   */
   List<H5Log> list(H5Log model);

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

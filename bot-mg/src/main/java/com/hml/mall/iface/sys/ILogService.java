package com.hml.mall.iface.sys;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.sys.Log;

/**
* <p>
    *  服务类
    * </p>
*
* @author hml
* @since 2021-04-05
*/


public interface ILogService extends IService<Log> {

   /**
   * 查询
   *
   * @param model
   * @return
   */
   List<Log> list(Log model);

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

package com.hml.mall.iface.sys;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.sys.LoginLog;

/**
* <p>
    *  服务类
    * </p>
*
* @author hml
* @since 2021-04-13
*/


public interface ILoginLogService extends IService<LoginLog> {

   /**
   * 查询
   *
   * @param model
   * @return
   */
   List<LoginLog> list(LoginLog model);

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

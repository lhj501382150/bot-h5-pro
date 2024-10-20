package com.hml.mall.iface.money;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.money.UsermoneyChange;

/**
* <p>
    *  服务类
    * </p>
*
* @author hml
* @since 2021-04-14
*/


public interface IUsermoneyChangeService extends IService<UsermoneyChange> {

   /**
   * 查询
   *
   * @param model
   * @return
   */
   List<UsermoneyChange> list(UsermoneyChange model);

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

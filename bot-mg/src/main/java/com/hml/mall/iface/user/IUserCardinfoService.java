package com.hml.mall.iface.user;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.user.UserCardinfo;

/**
* <p>
    *  服务类
    * </p>
*
* @author hml
* @since 2021-11-19
*/


public interface IUserCardinfoService extends IService<UserCardinfo> {

   /**
   * 查询
   *
   * @param model
   * @return
   */
   List<UserCardinfo> list(UserCardinfo model);

    /**
    * 分页查询
    *
    * @param model
    * @param pageNum  第几页
    * @param pageSize 每页数量
    * @return
    */
   PageResult findPage(PageRequest pageRequest);
   
   void auth(UserCardinfo model)throws Exception;

}

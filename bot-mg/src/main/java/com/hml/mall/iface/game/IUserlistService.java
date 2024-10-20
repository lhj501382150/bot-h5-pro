package com.hml.mall.iface.game;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.game.Userlist;

/**
* <p>
    *  服务类
    * </p>
*
* @author hml
* @since 2021-06-09
*/


public interface IUserlistService extends IService<Userlist> {

   /**
   * 查询
   *
   * @param model
   * @return
   */
   List<Userlist> list(Userlist model);

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

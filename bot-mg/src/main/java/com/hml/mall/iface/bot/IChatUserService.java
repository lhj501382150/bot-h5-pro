package com.hml.mall.iface.bot;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.bot.ChatUser;

/**
* <p>
    *  服务类
    * </p>
*
* @author hml
* @since 2023-12-08
*/


public interface IChatUserService extends IService<ChatUser> {

   /**
   * 查询
   *
   * @param model
   * @return
   */
   List<ChatUser> list(ChatUser model);

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

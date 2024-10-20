package com.hml.mall.iface.notice;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.notice.ForumRecv;

/**
* <p>
    *  服务类
    * </p>
*
* @author hml
* @since 2021-09-06
*/


public interface IForumRecvService extends IService<ForumRecv> {

   /**
   * 查询
   *
   * @param model
   * @return
   */
   List<ForumRecv> list(ForumRecv model);

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

package com.hml.mall.iface.notice;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.notice.Notice;

/**
* <p>
    *  服务类
    * </p>
*
* @author hml
* @since 2021-04-20
*/


public interface INoticeService extends IService<Notice> {

   /**
   * 查询
   *
   * @param model
   * @return
   */
   List<Notice> list(Notice model);

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

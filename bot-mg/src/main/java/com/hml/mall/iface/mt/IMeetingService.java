package com.hml.mall.iface.mt;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.mt.Meeting;

/**
* <p>
    *  服务类
    * </p>
*
* @author hml
* @since 2022-05-14
*/


public interface IMeetingService extends IService<Meeting> {

   /**
   * 查询
   *
   * @param model
   * @return
   */
   List<Meeting> list(Meeting model);

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

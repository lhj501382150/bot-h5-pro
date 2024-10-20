package com.hml.mall.iface.order;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.order.Order;

/**
* <p>
    *  服务类
    * </p>
*
* @author hml
* @since 2021-04-18
*/


public interface IOrderService extends IService<Order> {

   /**
   * 查询
   *
   * @param model
   * @return
   */
   List<Order> list(Order model);

    /**
    * 分页查询
    *
    * @param model
    * @param pageNum  第几页
    * @param pageSize 每页数量
    * @return
    */
   PageResult findPage(PageRequest pageRequest);
   
   PageResult findCount(PageRequest pageRequest);

   PageResult findFYCount(PageRequest pageRequest);
   
   void syncHisData() throws Exception;
}

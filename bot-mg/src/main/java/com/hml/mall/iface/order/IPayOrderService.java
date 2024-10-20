package com.hml.mall.iface.order;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.order.PayOrder;

/**
* <p>
    *  服务类
    * </p>
*
* @author hml
* @since 2021-07-10
*/


public interface IPayOrderService extends IService<PayOrder> {

   /**
   * 查询
   *
   * @param model
   * @return
   */
   List<PayOrder> list(PayOrder model);

    /**
    * 分页查询
    *
    * @param model
    * @param pageNum  第几页
    * @param pageSize 每页数量
    * @return
    */
   PageResult findPage(PageRequest pageRequest);
 
   void updateOrder(PayOrder order) throws Exception;
   /*
    * 查询订单状态
    */
   JSONObject queryOrderStatus(PayOrder order)throws Exception;
}

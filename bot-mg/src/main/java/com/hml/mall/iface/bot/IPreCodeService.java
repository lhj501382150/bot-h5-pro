package com.hml.mall.iface.bot;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.bot.PreCode;
import com.hml.mall.entity.order.Order;

/**
* <p>
    *  服务类
    * </p>
*
* @author hml
* @since 2024-11-03
*/


public interface IPreCodeService extends IService<PreCode> {

   /**
   * 查询
   *
   * @param model
   * @return
   */
   List<PreCode> list(PreCode model);

    /**
    * 分页查询
    *
    * @param model
    * @param pageNum  第几页
    * @param pageSize 每页数量
    * @return
    */
   PageResult findPage(PageRequest pageRequest);
   /*
    * 查询下注结果
    */
   List<Order> findDraw(String model,String contnum);
   
   List<Order> findDrawByNiu(String model,String contnum);

}

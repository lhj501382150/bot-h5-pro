package com.hml.mall.iface.trade;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.trade.WareJjHandorder;

/**
* <p>
    *  服务类
    * </p>
*
* @author hml
* @since 2021-09-30
*/


public interface IWareJjHandorderService extends IService<WareJjHandorder> {

   /**
   * 查询
   *
   * @param model
   * @return
   */
   List<WareJjHandorder> list(WareJjHandorder model);

    /**
    * 分页查询
    *
    * @param model
    * @param pageNum  第几页
    * @param pageSize 每页数量
    * @return
    */
   PageResult findPage(PageRequest pageRequest);
   
   void audit(WareJjHandorder model)throws Exception;
   
   void reject(WareJjHandorder model)throws Exception;

}

package com.hml.mall.iface.trade;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.trade.Basepara;
import com.hml.mall.entity.trade.Ware;
import com.hml.mall.entity.trade.WareAttr;
import com.hml.mall.entity.trade.WareSc;

/**
* <p>
    *  服务类
    * </p>
*
* @author hml
* @since 2021-04-17
*/


public interface IWareService extends IService<Ware> {

   /**
   * 查询
   *
   * @param model
   * @return
   */
   List<Ware> list(Ware model);

    /**
    * 分页查询
    *
    * @param model
    * @param pageNum  第几页
    * @param pageSize 每页数量
    * @return
    */
   PageResult findPage(PageRequest pageRequest);
   
   void saveWare(Ware ware)throws Exception;
   
   void updateWare(Ware ware) throws Exception;
   
   void delWare(Ware ware) throws Exception;
   
   List<WareAttr> findWareAttr(Ware ware)throws Exception;

   List<Basepara> findWareParas(Ware ware)throws Exception;
   
   List<WareSc> findWareSc(WareSc ware)throws Exception;
}

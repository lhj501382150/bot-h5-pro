package com.hml.mall.iface.order;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.order.Contract;
import com.hml.mall.entity.order.Invoic;
import com.hml.mall.entity.order.Sendinfo;

/**
* <p>
    *  服务类
    * </p>
*
* @author hml
* @since 2021-04-18
*/


public interface IContractService extends IService<Contract> {

   /**
   * 查询
   *
   * @param model
   * @return
   */
   List<Contract> list(Contract model);

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
    * 查询发货信息
    */
   Sendinfo findSendInfo(Contract contract);
   /*
    *  更新发货信息
    */
   boolean saveOrUpdateSend(Sendinfo model);
   /*
    * 发票信息
    */
   Invoic findInvoice(Contract contract);
   /*
    *  更新发货信息
    */
   boolean saveOrUpdateInvoic(Invoic model);

}

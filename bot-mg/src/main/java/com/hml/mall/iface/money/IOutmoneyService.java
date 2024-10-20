package com.hml.mall.iface.money;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.money.Outmoney;

/**
* <p>
    *  服务类
    * </p>
*
* @author hml
* @since 2021-11-19
*/


public interface IOutmoneyService extends IService<Outmoney> {

   /**
   * 查询
   *
   * @param model
   * @return
   */
   List<Outmoney> list(Outmoney model);

    /**
    * 分页查询
    *
    * @param model
    * @param pageNum  第几页
    * @param pageSize 每页数量
    * @return
    */
   PageResult findPage(PageRequest pageRequest);
   PageResult findExportPage(PageRequest pageRequest);
   /**
    * 审核提现记录
    * @param model
    * @throws Exception
    */
   void checkOutMoney(Outmoney model)throws Exception;

   
   void payOutMoney(List<Long> ids)throws Exception;
}

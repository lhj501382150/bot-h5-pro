package com.hml.mall.iface.trade;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.trade.WareFirmRule;

/**
* <p>
    *  服务类
    * </p>
*
* @author hml
* @since 2022-07-19
*/


public interface IWareFirmRuleService extends IService<WareFirmRule> {

   /**
   * 查询
   *
   * @param model
   * @return
   */
   List<WareFirmRule> list(WareFirmRule model);

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

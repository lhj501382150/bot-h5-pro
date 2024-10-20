package com.hml.mall.iface.trade;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hml.core.http.HttpResult;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.trade.Basepara;
import com.hml.mall.entity.trade.Ware;
import com.hml.mall.entity.trade.WareFirmPara;

/**
* <p>
    *  服务类
    * </p>
*
* @author hml
* @since 2022-07-18
*/


public interface IWareFirmParaService extends IService<WareFirmPara> {

   /**
   * 查询
   *
   * @param model
   * @return
   */
   List<WareFirmPara> list(WareFirmPara model);

    /**
    * 分页查询
    *
    * @param model
    * @param pageNum  第几页
    * @param pageSize 每页数量
    * @return
    */
   PageResult findPage(PageRequest pageRequest);
   
   List<Basepara> findWareParas(WareFirmPara item)throws Exception;

   HttpResult savePara(WareFirmPara item)throws Exception;
   HttpResult updatePara(WareFirmPara item)throws Exception;
   HttpResult deletePara(WareFirmPara item)throws Exception;
}

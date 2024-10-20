package com.hml.mall.iface.trade;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.trade.Wareclass;

/**
* <p>
    *  服务类
    * </p>
*
* @author hml
* @since 2021-04-17
*/


public interface IWareclassService extends IService<Wareclass> {

   /**
   * 查询
   *
   * @param model
   * @return
   */
   List<Wareclass> list(Wareclass model);

    /**
    * 分页查询
    *
    * @param model
    * @param pageNum  第几页
    * @param pageSize 每页数量
    * @return
    */
   PageResult findPage(PageRequest pageRequest);

  boolean saveWareclass(Wareclass entity)throws Exception ;
   

  boolean updateWareClass(Wareclass entity)throws Exception;
}

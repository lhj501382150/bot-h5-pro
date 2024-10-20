package com.hml.mall.iface.spread;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.spread.ActiveInfo;

/**
* <p>
    *  服务类
    * </p>
*
* @author hml
* @since 2022-07-31
*/


public interface IActiveInfoService extends IService<ActiveInfo> {

   /**
   * 查询
   *
   * @param model
   * @return
   */
   List<ActiveInfo> list(ActiveInfo model);

    /**
    * 分页查询
    *
    * @param model
    * @param pageNum  第几页
    * @param pageSize 每页数量
    * @return
    */
   PageResult findPage(PageRequest pageRequest);

   PageResult findUser(PageRequest pageRequest) throws Exception ;
   
   void syncActiveInfo(ActiveInfo item)throws Exception;
   
   void saveActiveInfo(ActiveInfo item)throws Exception;
   
   
}

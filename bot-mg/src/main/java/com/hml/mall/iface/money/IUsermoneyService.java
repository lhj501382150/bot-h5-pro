package com.hml.mall.iface.money;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.money.Usermoney;

/**
* <p>
    *  服务类
    * </p>
*
* @author hml
* @since 2021-04-14
*/


public interface IUsermoneyService extends IService<Usermoney> {

   /**
   * 查询
   *
   * @param model
   * @return
   */
   List<Usermoney> list(Usermoney model);

    /**
    * 分页查询
    *
    * @param model
    * @param pageNum  第几页
    * @param pageSize 每页数量
    * @return
    */
   PageResult findPage(PageRequest pageRequest);

   /**
    * 查询月财务统计
    * @param pageRequest
    * @return
    */
   PageResult findMonthPage(PageRequest pageRequest);

   List<Map<String,Object>> findChnageCount(Map<String,Object> paraMap) throws Exception;
}

package com.hml.mall.iface.money;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.money.UsermoneyChangeApply;

/**
* <p>
    *  服务类
    * </p>
*
* @author hml
* @since 2021-07-11
*/


public interface IUsermoneyChangeApplyService extends IService<UsermoneyChangeApply> {

   /**
   * 查询
   *
   * @param model
   * @return
   */
   List<UsermoneyChangeApply> list(UsermoneyChangeApply model);

    /**
    * 分页查询
    *
    * @param model
    * @param pageNum  第几页
    * @param pageSize 每页数量
    * @return
    */
   PageResult findPage(PageRequest pageRequest);
   
   void audit(UsermoneyChangeApply model)throws Exception;

   void saveAndAudit(UsermoneyChangeApply model)throws Exception;
}

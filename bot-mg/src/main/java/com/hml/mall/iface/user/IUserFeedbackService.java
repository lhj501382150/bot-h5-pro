package com.hml.mall.iface.user;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.user.UserFeedback;

/**
* <p>
    *  服务类
    * </p>
*
* @author hml
* @since 2021-07-17
*/


public interface IUserFeedbackService extends IService<UserFeedback> {

   /**
   * 查询
   *
   * @param model
   * @return
   */
   List<UserFeedback> list(UserFeedback model);

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

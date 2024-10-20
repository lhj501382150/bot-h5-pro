package com.hml.mall.iface.user;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hml.backcore.req.REQ518;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.user.UserLandinfo;

/**
* <p>
    *  服务类
    * </p>
*
* @author hml
* @since 2021-12-27
*/


public interface IUserLandinfoService extends IService<UserLandinfo> {

   /**
   * 查询
   *
   * @param model
   * @return
   */
   List<UserLandinfo> list(UserLandinfo model);

    /**
    * 分页查询
    *
    * @param model
    * @param pageNum  第几页
    * @param pageSize 每页数量
    * @return
    */
   PageResult findPage(PageRequest pageRequest);

   void issue(REQ518 req)throws Exception;
   
   void termination(REQ518 req)throws Exception;
}

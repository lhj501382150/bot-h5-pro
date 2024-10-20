package com.hml.mall.iface.user;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hml.backcore.req.REQ518;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.user.UserDigitart;

/**
* <p>
    *  服务类
    * </p>
*
* @author hml
* @since 2021-09-22
*/


public interface IUserDigitartService extends IService<UserDigitart> {

   /**
   * 查询
   *
   * @param model
   * @return
   */
   List<UserDigitart> list(UserDigitart model);

    /**
    * 分页查询
    *
    * @param model
    * @param pageNum  第几页
    * @param pageSize 每页数量
    * @return
    */
   PageResult findPage(PageRequest pageRequest);
   
   void post(REQ518 req)throws Exception; 

   int getMaxNo(int length,String pre);
}

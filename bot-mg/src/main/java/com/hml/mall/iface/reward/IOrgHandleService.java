package com.hml.mall.iface.reward;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.reward.OrgHandle;

/**
* <p>
    *  服务类
    * </p>
*
* @author hml
* @since 2021-05-26
*/


public interface IOrgHandleService extends IService<OrgHandle> {

   /**
   * 查询
   *
   * @param model
   * @return
   */
   List<OrgHandle> list(OrgHandle model);

    /**
    * 分页查询
    *
    * @param model
    * @param pageNum  第几页
    * @param pageSize 每页数量
    * @return
    */
   PageResult findPage(PageRequest pageRequest);

   void updateHandle(OrgHandle model) throws Exception;
}

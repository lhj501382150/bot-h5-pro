package com.hml.mall.iface.sys;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hml.mall.entity.sys.RoleMenu;

/**
* <p>
    *  服务类
    * </p>
*
* @author hml
* @since 2021-04-05
*/


public interface IRoleMenuService extends IService<RoleMenu> {

   /**
   * 查询
   *
   * @param model
   * @return
   */
   List<RoleMenu> list(RoleMenu model);

    /**
    * 分页查询
    *
    * @param model
    * @param pageNum  第几页
    * @param pageSize 每页数量
    * @return
    */
    IPage<RoleMenu> pageList(RoleMenu model, Integer pageNum, Integer pageSize);

}

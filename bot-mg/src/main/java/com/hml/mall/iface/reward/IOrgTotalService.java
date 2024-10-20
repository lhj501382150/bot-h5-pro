package com.hml.mall.iface.reward;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.reward.OrgTotal;

/**
* <p>
    *  服务类
    * </p>
*
* @author hml
* @since 2021-05-26
*/


public interface IOrgTotalService extends IService<OrgTotal> {

   /**
   * 查询
   *
   * @param model
   * @return
   */
   List<OrgTotal> list(OrgTotal model);

    /**
    * 分页查询
    *
    * @param model
    * @param pageNum  第几页
    * @param pageSize 每页数量
    * @return
    */
   PageResult findPage(PageRequest pageRequest);
   
   void countOrgTotal(Map<String,Object> map) throws Exception;
   
   void handleTotal(List<OrgTotal> list)throws Exception;

   void delOrgTotal(Map<String,Object> map)throws Exception;
}

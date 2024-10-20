package com.hml.mall.iface.user;



import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.user.User;

/**
* <p>
    *  服务类
    * </p>
*
* @author hml
* @since 2021-04-11
*/


public interface IUserService extends IService<User> {

   /**
   * 查询
   *
   * @param model
   * @return
   */
   List<User> list(User model);

    /**
    * 分页查询
    *
    * @param model
    * @param pageNum  第几页
    * @param pageSize 每页数量
    * @return
    */
   PageResult findPage(PageRequest pageRequest);
//添加用户
  	public boolean saveUser(User entity)throws Exception;
//      更新用户
  	public boolean updateUser(User entity)throws Exception;
//      删除用户
  	public boolean deleteUser(User entity)throws Exception ;
  	
  	public void updateStatus(User entity) throws Exception;
  	
  	public void clearMoney() throws Exception;
  	
  	List<User> initRelation() throws Exception;
  	
  	List<Map<String,Object>> findOrgCount()throws Exception;
  	
  	BigDecimal checkMoney() throws Exception;
  	
  	PageResult findUserLevelCount(PageRequest pageRequest)throws Exception;
}

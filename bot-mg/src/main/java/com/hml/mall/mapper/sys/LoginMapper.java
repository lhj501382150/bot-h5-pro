package com.hml.mall.mapper.sys;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hml.mall.entity.sys.Login;
import com.hml.mall.security.LoginUser;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hml
 * @since 2021-04-05
 */
public interface LoginMapper extends BaseMapper<Login> {

	List<Map<String,Object>> selectUserRoles(Long id);
	 
	@Select("select t.* from ("
			+ " select t1.*,t3.ROLE_NAME as roleName "
			+ " ,t2.uno1,t2.uno2,t2.uno3,t2.uno4,t2.uno5,t2.uno6,t2.uno7,t2.uno8,t2.uno9,t2.uno10,t2.uno11,t2.uno12,t2.uno13,t2.uno14 "
			+ " from TB_SYS_LOGIN t1 "
			+ " left join TB_SYS_USER_ROLE t4 on t1.LOGINNO = t4.LOGINNO "
			+ " left join TB_SYS_ROLE t3 on t4.ROLE_ID = t3.ROLE_ID"
			+ " left join TB_HY_USER_RELATION t2 on t1.USERNO = t2.USERNO "
			+ " ) t ${ew.customSqlSegment}")
	Page<Login> findPage(Page<Login> page,@Param(Constants.WRAPPER)Wrapper wrapper);
	
	LoginUser findLoginInfo(String loginNo);
}

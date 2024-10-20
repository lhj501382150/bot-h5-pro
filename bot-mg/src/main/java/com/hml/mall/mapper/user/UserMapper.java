package com.hml.mall.mapper.user;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hml.mall.entity.user.User;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hml
 * @since 2021-04-11
 */
public interface UserMapper extends BaseMapper<User> {

	@Select("select t.* from (	select t1.* ,COALESCE(t3.BALANCE,0) as BALANCE,COALESCE(t3.ENABLE,0) as ENABLE,COALESCE(t3.FREEZE,0) as FREEZE,COALESCE(t3.LIMTOUT,0) as LIMTOUT"
			+ "  ,t4.username as tjusername,t4.nickname as tjnickname "
			+ " ,t5.CLEVEL as clevel,t5.PARENTNO as parentno,t5.TJNO as tjno "
			+ " ,t5.uno1,t5.uno2,t5.uno3,t5.uno4,t5.uno5,t5.uno6,t5.uno7,t5.uno8,t5.uno9,t5.uno10,t5.uno11,t5.uno12,t5.uno13,t5.uno14 "
			+ "  from tb_hy_user t1  " + 
			"	left join ( SELECT USERNO, max( FDATE ) AS FDATE FROM tb_zj_usermoney GROUP BY USERNO ) t2 ON t1.USERNO = t2.USERNO " + 
			"	LEFT JOIN tb_zj_usermoney t3 ON t2.FDATE = t3.FDATE and t2.userno = t3.userno " + 
			"	LEFT JOIN tb_hy_user t4 ON t4.userno = t1.openid "
			+ " left join TB_HY_USER_RELATION t5 on t1.USERNO = t5.USERNO " + 
			"  ) t ${ew.customSqlSegment}")
	Page<User> findPage(Page<User> page,@Param(Constants.WRAPPER)Wrapper wrapper);
	
	@Select("select t1.*,t2.CLEVEL as clevel,t2.PARENTNO as parentno,t2.TJNO as tjno from TB_HY_USER t1 left join TB_HY_USER_RELATION t2 on t1.USERNO = t2.USERNO ${ew.customSqlSegment}")
	List<User> findUserList(@Param(Constants.WRAPPER)Wrapper wrapper);
	
	@Select("select t.* from (	select t1.* ,COALESCE(t3.BALANCE,0) as BALANCE,COALESCE(t3.ENABLE,0) as ENABLE,COALESCE(t3.FREEZE,0) as FREEZE"
			+ "  ,t4.username as tjusername,t4.nickname as tjnickname "
			+ " ,t5.CLEVEL as clevel,t5.PARENTNO as parentno,t5.TJNO as tjno " 
			+ " ,t5.uno1,t5.uno2,t5.uno3,t5.uno4,t5.uno5,t5.uno6,t5.uno7,t5.uno8,t5.uno9,t5.uno10,t5.uno11,t5.uno12,t5.uno13,t5.uno14 "
			+ "  from tb_hy_user t1  " + 
			"	left join ( SELECT USERNO, max( FDATE ) AS FDATE FROM tb_zj_usermoney GROUP BY USERNO ) t2 ON t1.USERNO = t2.USERNO " + 
			"	LEFT JOIN tb_zj_usermoney t3 ON t2.FDATE = t3.FDATE and t2.userno = t3.userno " + 
			"	LEFT JOIN tb_hy_user t4 ON t4.userno = t1.openid "
			+ " left join TB_HY_USER_RELATION t5 on t1.USERNO = t5.USERNO " + 
			"  ) t ${ew.customSqlSegment}")
	Page<User> findPageByUser(Page<User> page,@Param(Constants.WRAPPER)Wrapper wrapper);
	
	@Delete("delete from tb_zj_usermoney")
	void deleteMoney();
	
	@Delete("delete from tb_zj_usermoney_change ")
	void deleteChangeMoney();
	
	@Delete("delete from tb_zj_usermoney_change_apply ")
	void deleteChangeMoneyApply();
	
	@Select("select t.* from tb_hy_user t where userno not in (select userno from TB_HY_USER_RELATION )")
	List<User> findUnInitUser();
	
	List<Map<String,Object>> findOrgNumByUser(Map<String,Object> paraMap);
	
	@Select("select COALESCE(t3.ALLQUITY,0) as ENABLE from ( "
			+ "SELECT USERNO, max( FDATE ) AS FDATE FROM tb_zj_usermoney GROUP BY USERNO "
			+ ") t2 "
			+ "LEFT JOIN tb_zj_usermoney t3 ON t2.FDATE = t3.FDATE and t2.userno = t3.userno "
			+ "where t2.userno =#{userno} ")
	BigDecimal findEnableSelf(@Param("userno")String userno);
	
	
	Map<String,Object> findUseMoney(Map<String,Object> paraMap);
	
	List<Map<String,Object>> findUserLevelCount(Map<String,Object> paraMap);
	
	Map<String,Object> findUserLevelCountSum(Map<String,Object> paraMap);
	
	List<Map<String,Object>> findSubLevelCount(Map<String,Object> paraMap);
	
	Map<String,Object> findSubLevelCountSum(Map<String,Object> paraMap);
}


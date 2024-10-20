package com.hml.mall.mapper.spread;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hml.mall.entity.spread.ActiveInfo;
import com.hml.mall.entity.user.User;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hml
 * @since 2022-07-31
 */
public interface ActiveInfoMapper extends BaseMapper<ActiveInfo> {

	@Select(" select t.* from ("
			+ " select t1.*,t2.username,t3.sname,t4.warename from tb_jl_active_info t1 "
			+ " left join tb_hy_user t2 on t1.userno = t2.userno "
			+ " left join tb_jl_active t3 on t1.actid = t3.actid "
			+ " left join tb_jy_ware t4 on t3.wareno = t4.wareno "
			+ ") t ${ew.customSqlSegment}   ")
	Page<ActiveInfo> findPage(Page<ActiveInfo> page,@Param(Constants.WRAPPER) Wrapper wrapper);
	
//	@Select("select t.* from (select t1.*,t2.CLEVEL as clevel,t2.PARENTNO as parentno,t2.TJNO as tjno"
//			+ ",t3.tjnum,(case when t4.userno is null then 'N' else 'Y' end) as isCARD"
//			+ ",t2.uno1,t2.uno2,t2.uno3,t2.uno4,t2.uno5,t2.uno6,t2.uno7,t2.uno8,t2.uno9,t2.uno10,t2.uno11,t2.uno12,t2.uno13,t2.uno14 "
//			+ " from TB_HY_USER t1 "
//			+ " left join TB_HY_USER_RELATION t2 on t1.USERNO = t2.USERNO "
//			+ " left join (select tjno as userno,count(1) as tjnum from tb_hy_user_relation where tjno != '' group by tjno) t3 on t1.userno = t3.userno "
//			+ " left join (select distinct userno from tb_hy_user_cardinfo where CTYPE = 'C') t4 on t1.userno = t4.userno "
//			+ " where t1.userno not in(select userno from tb_jl_active_info where actid =#{actid}) "
//			+ " ) t ${ew.customSqlSegment}")
//	Page<User> findPageByUser(Page<?> page,@Param(Constants.WRAPPER)Wrapper wrapper,@Param("actid")long actid);
	
	List<User> findPageByUser(Map<String,Object> paraMap);
	Map<String,Object> findPageByUserCount(Map<String,Object> paraMap);
	
	List<Map<String,Object>> findUserByHold(Map<String,Object> map);
	
	List<User> findActive7(Map<String,Object> map);
	
	List<Map<String,Object>> findActive2(Map<String,Object> map);
}

package com.hml.mall.mapper.order;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hml.mall.entity.order.Order;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hml
 * @since 2021-04-18
 */
public interface OrderMapper extends BaseMapper<Order> {

	@Select("select t.* from ( "
			+ " select A.*,B.username,B.nickname,C.warename,D.username as tjusername,D.nickname as tjnickname "
			+ " from tb_jy_order A "
			+ " left join tb_hy_user B on A.userno = B.userno "
			+ " left join tb_jy_ware C on A.wareno = C.wareno "
			+ " left join tb_hy_user D on A.contno = D.userno "
			+ " ) t ${ew.customSqlSegment} order by ORDERNO desc ")
	Page<Order> findPage(Page<Order> page,@Param(Constants.WRAPPER)Wrapper wrapper);
	
	@Select("select t.* from (select t1.*,t2.username,t3.warename, "
			+ " t2.uno1,t2.uno2,t2.uno3,t2.uno4,t2.uno5,t2.uno6,t2.uno7,t2.uno8,t2.uno9,t2.uno10,t2.uno11,t2.uno12,t2.uno13,t2.uno14 "
			+ " from tb_jy_order t1 left join TB_HY_USER_RELATION t2 on t1.USERNO = t2.USERNO "
			+ " left join tb_jy_ware t3 on t1.wareno = t3.wareno) t ${ew.customSqlSegment} order by ORDERNO desc ")
	Page<Order> findPageByUser(Page<Order> page,@Param(Constants.WRAPPER)Wrapper wrapper);
	
	List<Map<String,Object>> findCount(Map<String,Object> paraMap);
	Map<String,Object> findCountSum(Map<String,Object> paraMap);
	
	List<Map<String,Object>> findFYCount(Map<String,Object> paraMap);
	Map<String,Object> findFYCountSum(Map<String,Object> paraMap);
	
	@Insert("INSERT INTO TB_JY_HIS_ORDER SELECT * FROM TB_JY_ORDER WHERE ORDTIME <= #{ordtime} AND ORDERNO NOT IN (SELECT ORDERNO FROM TB_JY_HIS_ORDER) ")
	void syncHisOrder(@Param("ordtime")String ordtime);
}

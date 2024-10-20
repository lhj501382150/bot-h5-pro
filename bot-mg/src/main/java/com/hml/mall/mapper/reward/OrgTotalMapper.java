package com.hml.mall.mapper.reward;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hml.mall.entity.reward.OrgTotal;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hml
 * @since 2021-05-26
 */
public interface OrgTotalMapper extends BaseMapper<OrgTotal> {

	@Select("select t.* from ( "
			+ " select t1.*,t2.username,t2.nickname from tb_jl_org_total t1 "
			+ " left join TB_HY_USER t2 on t1.ORGNO = t2.USERNO"
			+ " ) t ${ew.customSqlSegment}")
	Page<OrgTotal> findPage(Page<OrgTotal> page,@Param(Constants.WRAPPER)Wrapper wrapper);
	
	@Select("select sum(consumn) as consumn,sum(bonus) as bonus,sum(comms) as comms,sum(commbonus1) as commbonus1,sum(commbonus2) as commbonus2,sum(commbonus3) as commbonus3 from ("
			+ " select t1.*,t2.username from tb_jl_org_total t1 "
			+ " left join TB_HY_USER t2 on t1.ORGNO = t2.USERNO"
			+ " ) t ${ew.customSqlSegment}")
	OrgTotal selectSum(@Param(Constants.WRAPPER)Wrapper wrapper);
	
	void saveTotal(@Param("fdate")String fdate);
	
	List<Map<String,Object>> findOrgTotal(Map<String,Object> paraMap);
	List<Map<String,Object>> findOrgTotalSelf(Map<String,Object> paraMap);
	
	@Select("select count(1) from tb_jl_org_total  where ((btime <= #{btime} and etime >= #{etime}) "
			+ " or (btime <= #{btime} and etime >= #{btime})"
			+ " or (btime <= #{etime} and etime >= #{etime})"
			+ " or (btime >= #{btime} and etime <= #{etime})"
			+ ")")
	Integer check(@Param("btime")String btime,@Param("etime")String etime);
	
	@Select("select count(1) from tb_jl_org_total where status = 1 and btime = #{btime} and etime = #{etime} ")
	Integer checkHandle(@Param("btime")String btime,@Param("etime")String etime);
}

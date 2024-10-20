package com.hml.mall.mapper.user;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hml.mall.entity.user.UserFeedback;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hml
 * @since 2021-07-17
 */
public interface UserFeedbackMapper extends BaseMapper<UserFeedback> {

	@Select("select t.* from (select t1.*,t2.username,t3.username as recvname from  "
			+ "(SELECT ROW_NUMBER() OVER(partition by userno order by fid desc) as seq,t.*,t2.num FROM tb_hy_user_feedback t "
			+" left join (select count(1) as num,userno from tb_hy_user_feedback group by userno) t2  on t.userno = t2.userno "
			+" ) t1 "
			+ " left join tb_hy_user t2 on t1.userno = t2.userno "
			+ " left join tb_hy_user t3 on t1.recvno = t3.userno "
			+ ") t ${ew.customSqlSegment} order by fid desc")
	Page<UserFeedback> findPage(Page<UserFeedback> page,@Param(Constants.WRAPPER) Wrapper wrapper);
	
	@Select("select t.* from (select t1.*,t2.username,t3.username as recvname from tb_hy_user_feedback t1  "
			+ " left join tb_hy_user t2 on t1.userno = t2.userno "
			+ " left join tb_hy_user t3 on t1.recvno = t3.userno "
			+ ") t ${ew.customSqlSegment} order by fid desc")
	List<UserFeedback> findList(@Param(Constants.WRAPPER) Wrapper wrapper);
}

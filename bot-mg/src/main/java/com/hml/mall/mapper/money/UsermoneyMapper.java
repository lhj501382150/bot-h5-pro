package com.hml.mall.mapper.money;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hml.mall.entity.money.Usermoney;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hml
 * @since 2021-04-14
 */
public interface UsermoneyMapper extends BaseMapper<Usermoney> {

	
	@Select("select t.* from (select t1.*,t5.nickname,t2.username,t2.CLEVEL as clevel,t2.PARENTNO as parentno,t2.TJNO as tjno,t3.acctname,"
			+ "t2.uno1,t2.uno2,t2.uno3,t2.uno4,t2.uno5,t2.uno6,t2.uno7,t2.uno8,t2.uno9,t2.uno10,t2.uno11,t2.uno12,t2.uno13,t2.uno14 "
			+ " from tb_zj_usermoney t1 "
			+ " left join TB_HY_USER_RELATION t2 on t1.USERNO = t2.USERNO"
			+ " left join tb_zj_account t3 on t1.acctno = t3.acctno "
			+ " left join tb_hy_user t5 on t1.userno = t5.userno "
			+ ") t ${ew.customSqlSegment} order by fdate desc ")
	Page<Usermoney> findPageByUser(Page<Usermoney> page,@Param(Constants.WRAPPER)Wrapper wrapper);
	@Select("select sum(start) as start,sum(inmoney) as inmoney,sum(outmoney) as outmoney,sum(income) as income,sum(payment) as payment,sum(`in`) as `in`,sum(`out`) as `out` "
			+ " ,sum(balance) as balance,sum(freeze) as freeze,sum(lose) as lose,sum(enable) as enable,sum(allquity) as allquity, sum(limtout) as limtout,sum(canout) as canout "
			+ " from (select t1.*,t5.nickname,t2.username,t2.CLEVEL as clevel,t2.PARENTNO as parentno,t2.TJNO as tjno,t3.acctname,t4.orgtype,"
			+ "t2.uno1,t2.uno2,t2.uno3,t2.uno4,t2.uno5,t2.uno6,t2.uno7,t2.uno8,t2.uno9,t2.uno10,t2.uno11,t2.uno12,t2.uno13,t2.uno14 "
			+ " from tb_zj_usermoney t1 left join TB_HY_USER_RELATION t2 on t1.USERNO = t2.USERNO"
			+ " left join tb_hy_user t4 on t1.userno = t4.userno "
			+ " left join tb_zj_account t3 on t1.acctno = t3.acctno "
			+ " left join tb_hy_user t5 on t1.userno = t5.userno "
			+ ") t ${ew.customSqlSegment} order by fdate desc ")
	Usermoney findSum(@Param(Constants.WRAPPER)Wrapper wrapper);
	
	
	@Select("select t.* from ("
			+ " select t1.* ,t2.freeze,t2.balance,t2.enable,t3.inmoney,t3.outmoney,t3.income,t3.payment"
			+ " ,t4.username,t4.uno1,t4.uno2,t4.uno3,t4.uno4,t4.uno5,t4.uno6,t4.uno7,t4.uno8,t4.uno9,t4.uno10,t4.uno11,t4.uno12,t4.uno13,t4.uno14 "
			+ " ,t5.acctname,t6.orgtype  "
			+ " from (                                                                                                "
			+ " 	select left(fdate,7) as fdate,userno,acctno, start from tb_zj_usermoney                           "
			+ " 	where (fdate,userno,acctno) in (                                                                  "
			+ " 		select min(fdate),userno,acctno from tb_zj_usermoney group by left(fdate,7),userno,acctno     "
			+ " 	)) t1                                                                                             "
			+ " left join (                                                                                           "
			+ " 	select left(fdate,7) as fdate,userno,acctno, freeze,balance,enable from tb_zj_usermoney                  "
			+ " 	where (fdate,userno,acctno) in (                                                                  "
			+ " 		select max(fdate),userno,acctno from tb_zj_usermoney group by left(fdate,7),userno,acctno     "
			+ " 	)) t2 on t1.fdate = t2.fdate and t1.acctno = t2.acctno and t1.userno = t2.userno                  "
			+ " left join (                                                                                           "
			+ " 	select fmonth as fdate,userno,acctno                                                              "
			+ " 	,sum(INMONEY) as inmoney                                                                          "
			+ " 	,sum(outmoney) as outmoney                                                                        "
			+ " 	,sum(`in`) as income                                                                              "
			+ " 	,sum(`out`) as payment                                                                            "
			+ " 	from (                                                                                            "
			+ " 	select left(fdate,7) as fmonth, t1.* from tb_zj_usermoney t1                                      "
			+ " 	) t group by fmonth,userno,acctno                                                                 "
			+ " ) t3 on t1.fdate = t3.fdate and t1.acctno = t3.acctno and t1.userno = t3.userno                       "
			+ " left join tb_hy_user_relation t4 on t1.userno = t4.userno                                             "
			+ " left join tb_hy_user t6 on t1.userno = t6.userno                                             "
			+ " left join tb_zj_account t5 on t1.acctno = t5.acctno "
			+ ") t ${ew.customSqlSegment} order by fdate desc")
	Page<Usermoney> findMonthPage(Page<Usermoney> page,@Param(Constants.WRAPPER)Wrapper wrapper);
	
	List<Map<String,Object>> findChnageCount(Map<String,Object> paraMap);
	
	Map<String,Object> findPlatMoney(String fdate);
	
	@Insert("INSERT INTO tb_zj_his_usermoney SELECT * FROM tb_zj_usermoney WHERE fdate <= #{fdate} ")
	void syncHisData(@Param("fdate")String fdate);
}

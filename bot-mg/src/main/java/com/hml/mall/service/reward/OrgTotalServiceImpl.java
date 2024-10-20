package com.hml.mall.service.reward;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hml.core.page.MybatisPlusPageHelper;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.reward.OrgHandle;
import com.hml.mall.entity.reward.OrgParas;
import com.hml.mall.entity.reward.OrgTotal;
import com.hml.mall.iface.reward.IOrgTotalService;
import com.hml.mall.mapper.reward.OrgHandleMapper;
import com.hml.mall.mapper.reward.OrgParasMapper;
import com.hml.mall.mapper.reward.OrgTotalMapper;
import com.hml.mall.mapper.user.UserMapper;
import com.hml.mall.security.LoginUser;
import com.hml.mall.util.SecurityUtils;
import com.hml.utils.DateTimeUtils;
import com.hml.utils.StringUtils;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hml
 * @since 2021-05-26
 */

@Service("orgTotalService")
public class OrgTotalServiceImpl extends ServiceImpl<OrgTotalMapper, OrgTotal> implements IOrgTotalService {
	private Logger log = Logger.getLogger(OrgTotalServiceImpl.class);
	@Autowired
	private OrgTotalMapper orgTotalMapper;
	
	@Autowired
	private OrgParasMapper orgParasMapper;
	
	@Autowired
	private OrgHandleMapper orgHandleMapper;
	
	@Autowired
	private UserMapper userMapper;

    @Override
    public List< OrgTotal> list(OrgTotal model) {
       QueryWrapper< OrgTotal> wrapper = new QueryWrapper();
       return super.list(wrapper);
    }

    @Override
	public PageResult findPage(PageRequest pageRequest) {
    	LoginUser user = SecurityUtils.getLoginInfo();
		if(user.getType() > 0) {
			pageRequest.getParams().put("USERNO",user.getUserno());
		}
		PageResult pageResult =  MybatisPlusPageHelper.findPage(pageRequest, orgTotalMapper,"findPage");
		 if(pageResult.getContent().size() > 0) {//取合计
			 QueryWrapper qw = MybatisPlusPageHelper.getQueryWrapper(pageRequest);
			 OrgTotal sum = orgTotalMapper.selectSum(qw);
			 pageResult.setSum(sum);
		 }
		 
		 return pageResult;
	}
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleTotal(List<OrgTotal> list) throws Exception {
    	if(list.size() == 0) {
    		throw new Exception("请选择办理数据");
    	}
    	
    	for(OrgTotal item:list) {
    		OrgTotal total = orgTotalMapper.selectById(item.getWaterno());
    		if(total==null) {
    			throw new Exception(item.getPdate() +"-" + item.getOrgno()+"数据不存在");
    		}
    		if(total.getStatus()==1) {
    			throw new Exception(item.getPdate() +"-" + item.getOrgno()+"已办理");
    		}
//    		设置已办理
    		UpdateWrapper<OrgTotal> uw = new UpdateWrapper<OrgTotal>();
    		uw.eq("WATERNO", item.getWaterno());
    		uw.set("STATUS", 1);
    		orgTotalMapper.update(null, uw);
//    		插入办理数据
    		saveHandle(total, 1, total.getUserno1(), total.getCommbonus1());
    		if(!StringUtils.isBlank(total.getUserno2())) {
    			saveHandle(total, 2, total.getUserno2(), total.getCommbonus2());
    		}
    		if(!StringUtils.isBlank(total.getUserno3())) {
    			saveHandle(total, 3, total.getUserno3(), total.getCommbonus3());
    		}
    	}
    	
    }
    
    private void saveHandle(OrgTotal total,int clevel,String userno,BigDecimal commbouns) {
		OrgHandle handle = new OrgHandle();
		handle.setBtime(total.getBtime());
		handle.setEtime(total.getEtime());
		handle.setTotalno(total.getWaterno());
		handle.setOrgno(userno);
		handle.setClevel(clevel); 
		handle.setComms(total.getComms());
		handle.setCommbonus(commbouns);
		handle.setPstatus("0");
		handle.setPdate(DateTimeUtils.getCurrentDate("yyyy-MM-dd"));
		orgHandleMapper.insert(handle);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delOrgTotal(Map<String, Object> map) throws Exception {
    	String btime = map.get("btime").toString();
    	String etime = map.get("etime").toString();
    	Integer count = orgTotalMapper.checkHandle(btime, etime);
    	if(count > 0) {
    		throw new Exception("该时间段已办理，暂不能删除");
    	}
    	QueryWrapper<OrgTotal> qw = new QueryWrapper<OrgTotal>();
    	qw.eq("btime", btime);
    	qw.eq("etime", etime);
    	
    	orgTotalMapper.delete(qw);
     }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void countOrgTotal(Map<String,Object> map) throws Exception {
    	String btime = map.get("btime").toString();
    	String etime = map.get("etime").toString();
    	
    	Integer count = orgTotalMapper.check(btime, etime);
    	if(count > 0) {
    		throw new Exception("该时间段已办理，请勿重复计算");
    	}
    	QueryWrapper<OrgTotal> qw = new QueryWrapper<OrgTotal>();
    	qw.eq("BTIME", btime);
    	qw.eq("ETIME", etime);
    	orgTotalMapper.delete(qw);
//    	查询待结算数据
    	List<Map<String, Object>> contractList = findOrgTotal(map);
//    	加载机构参数
    	Map<String,OrgParas> paras = loadOrgParas();
//    	计算
    	List<OrgTotal> totalList = count(btime,etime, contractList, paras);
//    	保存计算结果
    	if(totalList.size() > 0) {
    		super.saveBatch(totalList);
    	}else {
    		throw new Exception("没有需要计算数据");
    	}
    }
    
    private List<OrgTotal> count(String btime,String etime,List<Map<String,Object>> list,Map<String,OrgParas> para)throws Exception{
//    	分条计算 业绩统计 每一笔数据上有不同的上级 计算每一个上级的业绩统计
    	List<OrgTotal> totalList = new ArrayList<OrgTotal>();
    	for(Map<String,Object> map: list){
    		log.info("计算返还手续费统计:" + map.toString());
    		BigDecimal comms = fnum(map.get("COMM"));
        	
//        	封装业绩统计
        	OrgTotal total = new OrgTotal();
        	total.setBtime(btime);
        	total.setEtime(etime);
        	total.setComms(comms);
        	
        	OrgParas orgPara1 = para.get("D1");
        	if(orgPara1 != null && !StringUtils.isBlank(map.get("USERNO1"))) {
        		total.setCrate1(orgPara1.getMrate());
            	total.setCommbonus1(countRate(comms,orgPara1.getMrate()));
            	total.setUserno1(map.get("USERNO1").toString());
        	}else {
        		continue;
        	}
        	OrgParas orgPara2 = para.get("D2");
        	if(orgPara2 != null && !StringUtils.isBlank(map.get("USERNO2"))){
        		total.setCrate2(orgPara2.getMrate());
            	total.setCommbonus2(countRate(comms,orgPara2.getMrate()));
            	total.setUserno2(map.get("USERNO2").toString());
        	}
        	OrgParas orgPara3 = para.get("D3");
        	if(orgPara2 != null && !StringUtils.isBlank(map.get("USERNO3"))){
        		total.setCrate3(orgPara3.getMrate());
            	total.setCommbonus3(countRate(comms,orgPara3.getMrate()));
            	total.setUserno3(map.get("USERNO3").toString());
        	}
        	total.setStatus(0);
        	total.setPdate(DateTimeUtils.getCurrentDate("yyyy-MM-dd"));
        	total.setOrgno(map.get("USERNO").toString());
        	totalList.add(total);
    	}
    	return totalList;
    }
    
    private List<Map<String,Object>> findOrgTotal(Map<String,Object> map)throws Exception{
    	List<Map<String, Object>> list = orgTotalMapper.findOrgTotal(map);
    	if(list == null || list.size()==0){
    		throw new Exception("没有需要计算数据");
    	}
    	return list;
    }
    
    private Map<String,OrgParas> loadOrgParas()throws Exception{
    	Map<String,OrgParas> map = new HashMap<String, OrgParas>();
    	QueryWrapper<OrgParas> qw = new QueryWrapper<>();
    	List<OrgParas> list = orgParasMapper.selectList(qw);
    	if(list ==null || list.size() ==0){
    		throw new Exception("未加载推荐返还参数");
    	}
    	for(OrgParas para: list){
    		int clevel = para.getClevel();
    		map.put("D"+clevel, para);
    	}
    	return map;
    }
    
    private BigDecimal fnum(Object obj)throws Exception{
    	if(obj==null) obj = 0;
    	DecimalFormat df = new DecimalFormat("##.######");
    	String s = df.format(obj);
    	return new BigDecimal(s);
    }
    
    private BigDecimal countRate(BigDecimal num,BigDecimal rate)throws Exception{
    	BigDecimal ret = num.multiply(rate).divide(fnum(100)).setScale(2, RoundingMode.HALF_UP);
    	return ret;
    }
}

package com.hml.mall.service.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hml.core.page.MybatisPlusPageHelper;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.money.HisUsermoney;
import com.hml.mall.entity.money.HisUsermoneyChange;
import com.hml.mall.entity.money.UsermoneyChange;
import com.hml.mall.entity.order.HisOrder;
import com.hml.mall.entity.user.User;
import com.hml.mall.entity.user.UserRelation;
import com.hml.mall.iface.order.IHisOrderService;
import com.hml.mall.mapper.money.HisUsermoneyChangeMapper;
import com.hml.mall.mapper.money.HisUsermoneyMapper;
import com.hml.mall.mapper.order.HisOrderMapper;
import com.hml.mall.mapper.sys.UserRoleMapper;
import com.hml.mall.mapper.user.UserMapper;
import com.hml.mall.mapper.user.UserRelationMapper;
import com.hml.mall.security.LoginUser;
import com.hml.mall.util.SecurityUtils;
import com.hml.utils.DateTimeUtils;
import com.hml.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hml
 * @since 2021-04-18
 */
@Slf4j
@Service("hisOrderService")
public class HisOrderServiceImpl extends ServiceImpl<HisOrderMapper, HisOrder> implements IHisOrderService {

	@Autowired
	private HisOrderMapper hisOrderMapper;
	
	@Autowired
	private HisUsermoneyMapper hisUsermoneyMapper;
	
	@Autowired
	private HisUsermoneyChangeMapper hisUsermoneyChangeMapper;
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private UserRelationMapper userRelationMapper;
	
    @Override
    public List<HisOrder> list(HisOrder model) {
       QueryWrapper<HisOrder> wrapper = new QueryWrapper();
       return super.list(wrapper);
    }

    @Override
	public PageResult findPage(PageRequest pageRequest) {
    	LoginUser user = SecurityUtils.getLoginInfo();
		if(user==null) {
			return null;
		}
		if(user.getType()<=0) {
			return MybatisPlusPageHelper.findPage(pageRequest, hisOrderMapper,"findPage");
		}else {
			pageRequest.getParams().put(user.getQueryNo(), SecurityUtils.getUsername());
			return MybatisPlusPageHelper.findPage(pageRequest, hisOrderMapper,"findPageByUser");
		}
	}
    @Override
    public PageResult findCount(PageRequest pageRequest) {
    	LoginUser user = SecurityUtils.getLoginInfo();
		if(user==null) {
			return null;
		}
    	Map<String, Object> params = pageRequest.getParams();
    	int pageNum = pageRequest.getPageNum();
     	int size = pageRequest.getPageSize();
     	int pageIdx = (pageNum - 1) * size;
     	params.put("pageIndex", pageIdx);
     	params.put("pageSize", size);
     	if(user.getType() > 0) {
     		params.put(user.getQueryNo(), SecurityUtils.getUsername());
     	}
     	Map<String, Object> count = hisOrderMapper.findCountSum(params);
     	List<Map<String, Object>> datas = hisOrderMapper.findCount(params);
     	
     	PageResult result = new PageResult();
     	result.setContent(datas);
     	int total = Integer.parseInt(count.get("TOTAL").toString());
     	int totalPage = total/size +1;
     	result.setTotalPages(totalPage);
     	result.setPageNum(pageNum);
     	result.setPageSize(size);
     	result.setTotalSize(total);
     	result.setSum(count);
     	return result;
    }
    
    @Override
    public PageResult findFYCount(PageRequest pageRequest) {
    	LoginUser user = SecurityUtils.getLoginInfo();
		if(user==null) {
			return null;
		}
    	Map<String, Object> params = pageRequest.getParams();
    	int pageNum = pageRequest.getPageNum();
     	int size = pageRequest.getPageSize();
     	int pageIdx = (pageNum - 1) * size;
     	params.put("pageIndex", pageIdx);
     	params.put("pageSize", size);
     	if(user.getType() > 0) {
     		params.put(user.getQueryNo(), SecurityUtils.getUsername());
     	}
     	Map<String, Object> count = hisOrderMapper.findFYCountSum(params);
     	List<Map<String, Object>> datas = hisOrderMapper.findFYCount(params);
     	
     	PageResult result = new PageResult();
     	result.setContent(datas);
     	int total = Integer.parseInt(count.get("TOTAL").toString());
     	int totalPage = total/size +1;
     	result.setTotalPages(totalPage);
     	result.setPageNum(pageNum);
     	result.setPageSize(size);
     	result.setTotalSize(total);
     	result.setSum(count);
     	return result;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
	public void deleteHisData(String bdate,String edate) throws Exception {
//    	String bdate = month + "-01";
//    	String edate = DateTimeUtils.lastDayOfMonth(month, "yyyy-MM");
    	String btime = bdate + " 07:00:00";
    	String etime = edate + " 06:00:00";
		log.info("删除历史数据:{} ~ {}",bdate,edate);
		QueryWrapper<HisOrder> qw = new QueryWrapper<HisOrder>();
        qw.ge("ORDTIME", btime);
        qw.le("ORDTIME", etime);
        hisOrderMapper.delete(qw);
        
        QueryWrapper<HisUsermoney> qw1 = new QueryWrapper<HisUsermoney>();
        QueryWrapper<HisUsermoneyChange> qw2 = new QueryWrapper<HisUsermoneyChange>();
        
        qw1.ge("FDATE", bdate);
        qw1.le("FDATE", edate);
        hisUsermoneyMapper.delete(qw1);
        
        qw2.ge("FDATE", bdate);
        qw2.le("FDATE", edate);
        hisUsermoneyChangeMapper.delete(qw2);
	}
    
    @Override
   	public PageResult findScorePage(PageRequest pageRequest) {
       	LoginUser user = SecurityUtils.getLoginInfo();
   		if(user==null) {
   			return null;
   		}
   		if(user.getType() > 0) {
//   			分销商/客户 查询 配置当前登陆信息
   			pageRequest.getParams().put(user.getQueryNo(), SecurityUtils.getUsername());
   		}
   		PageResult result = MybatisPlusPageHelper.findPage(pageRequest, hisUsermoneyChangeMapper,"findPageByUser");
   		if(result.getContent() != null && result.getContent().size() > 0) {
   			QueryWrapper<?> qw = MybatisPlusPageHelper.getQueryWrapper(pageRequest);
   			HisUsermoneyChange sum = hisUsermoneyChangeMapper.findSum(qw);
   			result.setSum(sum);
   		}
   		return result;
   		
   		
   	}
    
    @Override
	@Transactional(rollbackFor = Exception.class)
	public PageResult findUserLevelCount(PageRequest pageRequest) throws Exception {
		Integer page = pageRequest.getPageNum();
		Integer size = pageRequest.getPageSize();
		page = (page-1) * size;
		Map<String, Object> params = pageRequest.getParams();
		if(params == null) {
			params = new HashMap<String, Object>();
		}
		params.put("page", page);
		params.put("size", size);
		List<Map<String, Object>> datas = new ArrayList<Map<String,Object>>();
		PageResult pageResult = new PageResult();
		Integer count = datas.size();
		if(!StringUtils.isBlank(params.get("parentno"))){
			String pno = params.get("parentno").toString();
			User puser = userMapper.selectById(pno);
			if(puser == null || "2".equals(puser.getOrgtype())) {
				return pageResult;
			}
			UserRelation user = userRelationMapper.selectById(pno);
			params.put("pno", "uno" + user.getClevel());
			int subLevel = user.getClevel() + 1;
			params.put("uno", "uno" + subLevel);
			
			datas = hisOrderMapper.findSubLevelCount(params);
			if(datas!=null  && datas.size()>0) {
				Map<String, Object> sum = hisOrderMapper.findSubLevelCountSum(params);
				pageResult.setSum(sum);
				count = Integer.parseInt(sum.get("TOTAL").toString());
			}
		}else {
			datas = hisOrderMapper.findUserLevelCount(params);
			if(datas!=null  && datas.size()>0) {
				Map<String, Object> sum = hisOrderMapper.findUserLevelCountSum(params);
				pageResult.setSum(sum);
				count = Integer.parseInt(sum.get("TOTAL").toString());
			}
		}
		
		pageResult.setContent(datas);
		pageResult.setPageNum(page);
		pageResult.setPageSize(size);
		pageResult.setTotalSize(count);
		return pageResult;
	}	
}

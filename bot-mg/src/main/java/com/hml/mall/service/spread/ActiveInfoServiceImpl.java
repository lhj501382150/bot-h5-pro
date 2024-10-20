package com.hml.mall.service.spread;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hml.backcore.config.BackCoreConfig;
import com.hml.backcore.req.REQ518;
import com.hml.backcore.utils.ZbUtils;
import com.hml.core.page.MybatisPlusPageHelper;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.spread.Active;
import com.hml.mall.entity.spread.ActiveInfo;
import com.hml.mall.entity.user.User;
import com.hml.mall.iface.spread.IActiveInfoService;
import com.hml.mall.mapper.spread.ActiveInfoMapper;
import com.hml.mall.mapper.spread.ActiveMapper;
import com.hml.mall.util.HttpUtils;
import com.hml.mall.util.IPUtils;
import com.hml.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hml
 * @since 2022-07-31
 */

@Service("activeInfoService")
@Slf4j
public class ActiveInfoServiceImpl extends ServiceImpl<ActiveInfoMapper, ActiveInfo> implements IActiveInfoService {

	@Autowired
	private ActiveInfoMapper activeInfoMapper;
	
	@Autowired
	private ActiveMapper activeMapper;

    @Override
    public List< ActiveInfo> list(ActiveInfo model) {
       QueryWrapper< ActiveInfo> wrapper = new QueryWrapper();
       return super.list(wrapper);
    }

    @Override
	public PageResult findPage(PageRequest pageRequest) {
		 
		return MybatisPlusPageHelper.findPage(pageRequest, activeInfoMapper,"findPage");
	}

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void syncActiveInfo(ActiveInfo item) throws Exception {
    	QueryWrapper<ActiveInfo> qw = new QueryWrapper<ActiveInfo>();
    	qw.eq("ACTID", item.getActid());
    	qw.eq("USERNO", item.getUserno());
    	qw.eq("SYCN", "Y");
    	Integer count = activeInfoMapper.selectCount(qw);
    	if(count > 0) {
    		throw new Exception("该数据已同步！");
    	}
    	REQ518 req = new REQ518();
    	req.setLOGINNO(item.getUserno());
    	req.setUSERNO(item.getUserno());
    	req.setDETYPE("4");
    	req.setDEVICENO("");
    	req.setIP(IPUtils.getIpAddr(HttpUtils.getHttpServletRequest()));
    	req.setOPTYPE("7");
    	req.setMEM(String.valueOf(item.getActid()));
	    	
       JSONObject ret = ZbUtils.post(BackCoreConfig.TRADE518, req);
//       item.setSycn("Y");
//       item.setArtid(ret.getString("DATA"));
//       UpdateWrapper<ActiveInfo> uw = new UpdateWrapper<ActiveInfo>();
//       uw.eq("ACTID", item.getActid());
//       uw.eq("USERNO", item.getUserno());
//       activeInfoMapper.update(item, uw);
       
       log.info("更新数据库状态完成：" + item);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
	public void saveActiveInfo(ActiveInfo item) throws Exception {
    	log.info("活动奖励条件：{}",item.getParams());
		if(item.getActid() == null) {
			throw new Exception("活动编号不能为空！");
		}
		Active active = activeMapper.selectById(item.getActid());
		if(active == null) {
			throw new Exception("请输入正确的活动编号！");
		}
//		if("5".equals(active.getActype()) || "6".equals(active.getActype())) {
//			throw new Exception("该活动类型暂不支持批量录入");
//		}
// 		判断条件  注册日期  客户编号 客户名称 所属机构  推荐人编号 推荐人数 登录次数  认证状态  是否绑定银行卡 订单日期
		List<User> users = null;
		if("7".equals(active.getActype())) {
			if(!StringUtils.isBlank(item.getParams().get("start")) && !StringUtils.isBlank(item.getParams().get("end"))) {
				int beg = Integer.parseInt(item.getParams().get("start").toString());
				int size = Integer.parseInt(item.getParams().get("end").toString());
				beg = beg -1;
				size = size - beg;
				item.getParams().put("start", String.valueOf(beg));
				item.getParams().put("end", String.valueOf(size));
			}
			users = activeInfoMapper.findActive7(item.getParams());
		}else {
			item.getParams().put("actid", item.getActid());
			users = activeInfoMapper.findPageByUser(item.getParams());
		}
		if(users == null || users.size() == 0) {
			throw new Exception("没有符合条件的数据！");
		}
//		含有商品 并且大于数量 
		if(!StringUtils.isBlank(item.getParams().get("wareno"))) {
			List<String> usernos = getUserno(users);
			log.info("客户信息：" + usernos);
			List<Map<String,Object>> wares = (List<Map<String,Object>>)(item.getParams().get("wareno"));
			int length = wares.size();
			
			List<String> warenos = new ArrayList<String>();
			Map<String,Integer> wareMap = new HashedMap();
	    	for(Map<String,Object> ware: wares) {
	    		if(StringUtils.isBlank(ware.get("WARENO"))) continue;
	    		String wareNo = ware.get("WARENO").toString();
	    		warenos.add(wareNo);
	    		try {
	    			wareMap.put(wareNo, Integer.parseInt(ware.get("NUM").toString()));
				} catch (Exception e) {
					throw new Exception("请设置正确的商品数量！");
				}
	    	}
			log.info("活动奖励商品条件：" + wareMap);
			Map<String,Object> paraMap = new HashedMap();
			paraMap.put("userno",usernos);
			paraMap.put("wareno",warenos);
			paraMap.put("no",length);
			List<Map<String, Object>> datas = null;
			if("2".equals(active.getActype())) {
				datas = activeInfoMapper.findActive2(paraMap);
			}else {
				datas = activeInfoMapper.findUserByHold(paraMap);
			}
					
			for(Map<String, Object> data: datas) {
				log.info("满足持有种类：" + data);
				String userno = data.get("USERNO").toString();
				String wareData = data.get("INFO").toString();
				Integer num = getNum(active, null, wares, wareData);
				if(num > 0){
					ActiveInfo model = new ActiveInfo();
					model.setActid(item.getActid());
					model.setUserno(userno);
					model.setSycn("N");
					model.setNum(num);
					activeInfoMapper.insert(model);
				}
			}
		}else {
			log.info("客户数量为：【{}】",users.size());
			for(User user : users) {
				ActiveInfo model = new ActiveInfo();
				model.setActid(item.getActid());
				model.setUserno(user.getUserno());
				model.setSycn("N");
				model.setNum(getNum(active, user, null,""));
				activeInfoMapper.insert(model);
			}
		}
		
	}
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public PageResult findUser(PageRequest pageRequest) throws Exception {
    	if(StringUtils.isBlank(pageRequest.getParam("actid"))) {
    		throw new Exception("请选择活动");
    	}
    	Map<String, Object> params = pageRequest.getParams();
    	int pageNum = pageRequest.getPageNum();
    	int size = pageRequest.getPageSize();
    	int pageIdx = (pageNum - 1) * size;
    	params.put("pageIndex", pageIdx);
    	params.put("pageSize", size);
    	
    	Map<String, Object> count = activeInfoMapper.findPageByUserCount(params);
    	
    	List<User> datas = activeInfoMapper.findPageByUser(params);
    	
    	PageResult result = new PageResult();
    	result.setContent(datas);
    	int total = Integer.parseInt(count.get("TOTAL").toString());
    	int totalPage = total/size +1;
    	result.setTotalPages(totalPage);
    	result.setPageNum(pageNum);
    	result.setPageSize(size);
    	result.setTotalSize(total);
    	return result; 
    }
    
    private List<String> getUserno(List<User> users){
    	List<String> usernos = new ArrayList<String>();
    	for(User item: users) {
    		usernos.add(item.getUserno());
    	}
    	return usernos;
    }
     
    private Integer getNum(Active active, User user,List<Map<String,Object>> wares,String info)throws Exception{
    	Integer num = 0;
    	 
    	return num;
    }
    
}

package com.hml.mall.service.user;



import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hml.backcore.config.BackCoreConfig;
import com.hml.core.page.MybatisPlusPageHelper;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.sys.Login;
import com.hml.mall.entity.sys.UserRole;
import com.hml.mall.entity.user.User;
import com.hml.mall.entity.user.UserLimit;
import com.hml.mall.entity.user.UserRelation;
import com.hml.mall.iface.user.IUserLimitService;
import com.hml.mall.iface.user.IUserService;
import com.hml.mall.mapper.sys.LoginMapper;
import com.hml.mall.mapper.sys.UserRoleMapper;
import com.hml.mall.mapper.user.UserLimitMapper;
import com.hml.mall.mapper.user.UserMapper;
import com.hml.mall.mapper.user.UserRelationMapper;
import com.hml.mall.security.LoginUser;
import com.hml.mall.util.PasswordEncoder;
import com.hml.mall.util.SecurityUtils;
import com.hml.redis.RedisKey;
import com.hml.redis.RedisUtils;
import com.hml.utils.HttpClientUtils;
import com.hml.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hml
 * @since 2021-04-11
 */
@Slf4j
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private UserRoleMapper userRoleMapper;
	
	@Autowired
	private LoginMapper loginMapper;
	
	@Autowired
	private UserRelationMapper userRelationMapper;
	
	@Autowired
	private IUserLimitService userLimitService;
	
	@Autowired
	private RedisUtils redisUtils;
	
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
			
			datas = userMapper.findSubLevelCount(params);
			if(datas!=null  && datas.size()>0) {
				Map<String, Object> sum = userMapper.findSubLevelCountSum(params);
				pageResult.setSum(sum);
				count = Integer.parseInt(sum.get("TOTAL").toString());
			}
		}else {
			datas = userMapper.findUserLevelCount(params);
			if(datas!=null  && datas.size()>0) {
				Map<String, Object> sum = userMapper.findUserLevelCountSum(params);
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

	@Override
	@Transactional(rollbackFor = Exception.class)
	public List<User> initRelation() throws Exception {
		 List<User> users = userMapper.findUnInitUser();
		 Map<String,UserRelation> relationMap = new HashMap<String,UserRelation>();
		 for(User user : users) {
			 try {
				 UserRelation relation = getUserRelation(user,relationMap);
				 userRelationMapper.insert(relation);
				 if(!StringUtils.isBlank(user.getOpenid())) {
					 User tjuser = userMapper.selectById(user.getOpenid());
					 if(tjuser.getUsertype() - user.getUsertype() != 0) {
						 user.setUsertype(tjuser.getUsertype());
						 userMapper.updateById(user);
					 }
				 }
				 redisUtils.hset(RedisKey.USER_INVITE_AUTH, user.getUserno(), String.valueOf(user.getUsertype()));
				 log.info("初始化层级信息：【{}】-{}",relation.getUserno(),relation.getTjno());
			} catch (Exception e) {
				log.error("初始化层级信息异常:{}",e);
			}
		 }
		return null;
	}
    @Override
    public List< User> list(User model) {
       QueryWrapper< User> wrapper = new QueryWrapper();
       if(model!=null) {
    	   if(!StringUtils.isBlank(model.getUserno())) {
    		   wrapper.eq("USERNO", model.getUserno());
    	   }
    	   if(model.getOrgtype() !=null) {
    		   wrapper.eq("ORGTYPE", model.getOrgtype());
    	   }
    	   if(!StringUtils.isBlank(model.getIsvalid())) {
    		   wrapper.eq("ISVALID", model.getIsvalid());
    	   }
       }
       LoginUser user = SecurityUtils.getLoginInfo();
    	 if(user.getType() > 0) {
    		int level = user.getClevel();
    		wrapper.eq("UNO" + level, user.getUserno());
    		 if(model.getClevel()!=null) {
      		   wrapper.eq("clevel", model.getClevel());
      	 }
    	 }else {
    		 if(model.getClevel()!=null) {
    		   wrapper.eq("clevel", model.getClevel());
    	   }
    	 }
  	   
       List<User> list = userMapper.findUserList(wrapper);
       return list;
    }

    @Override
	public PageResult findPage(PageRequest pageRequest) {
		LoginUser user = SecurityUtils.getLoginInfo();
		if(user==null) {
			return null;
		}
		QueryWrapper<User> qw = new QueryWrapper<User>();
		if(!StringUtils.isBlank(pageRequest.getParam("sex"))) {
			qw.ge("sex", pageRequest.getParam("sex"));
		}
		if(!StringUtils.isBlank(pageRequest.getParam("balance"))) {
			qw.ge("balance", pageRequest.getParam("balance"));
		}
		if(!StringUtils.isBlank(pageRequest.getParam("userno"))) {
			String userno = pageRequest.getParam("userno").toString();
			qw.and(wrapper -> {
				wrapper.eq("userno", userno).or().like("username", userno).or().like("nickname", userno);
			});
		}
		if(!StringUtils.isBlank(pageRequest.getParam("openid"))) {
			String userno = pageRequest.getParam("openid").toString();
			qw.and(wrapper -> {
				wrapper.eq("openid", userno).or().like("tjusername", userno).or().like("tjnickname", userno);
			});
		}
		if(!StringUtils.isBlank(pageRequest.getParam("clevel"))) {
    		Integer clevel = Integer.parseInt(pageRequest.getParam("clevel").toString());
    		if(clevel < 0) {
    			if(clevel == -2) {
    				pageRequest.getParams().put("parentno",user.getUserno());
    			}
    			pageRequest.getParams().remove("clevel");
    		}
    	}
		if(user.getType() <= 0) {
			return MybatisPlusPageHelper.findPage(pageRequest, userMapper,"findPage");
		}else {
//			机构/客户 查询 配置当前登陆信息
			pageRequest.getParams().put("userno@ne",user.getUserno());
			pageRequest.getParams().put(user.getQueryNo(), SecurityUtils.getUsername());
			return MybatisPlusPageHelper.findPage(pageRequest, userMapper,"findPageByUser");
			
		}
	}

    @Override
    @Transactional(rollbackFor = Exception.class)
	public boolean saveUser(User entity)throws Exception {
    	LoginUser loginUser = SecurityUtils.getLoginInfo();
    	if(loginUser.getType() > 0) {
    		if(StringUtils.isBlank(entity.getParentno())) {
    			throw new Exception("所属代理不能为空");
    		}
    	}
//		判断账号是否已存在
    	User user = userMapper.selectById(entity.getUserno());
    	if(user != null) {
    		throw new Exception("客户编号已注册");
    	}
 
    	if(!StringUtils.isBlank(entity.getOpenid())) {
    		User tjuser = userMapper.selectById(entity.getOpenid());
    		if(tjuser == null) {
    			throw new Exception("推荐用户不存在");
    		}
    	}
    	
//    	判断登陆账号是否存在
    	QueryWrapper<Login> qw = new QueryWrapper<Login>(); 
    	qw.eq("LOGINNO", entity.getUserno());
    	Login login = loginMapper.selectOne(qw);
    	if(login != null) {
    		throw new Exception("该账号已存在");
    	}
    	

    	 
//    	插入登陆用户
    	login = new Login();
    	login.setLoginno(entity.getUserno());
    	login.setUserno(entity.getUserno());
    	login.setLoginname(entity.getUsername());
//    	初始化密码 默认证件号码后6 位，不足6位 为 111111
    	String card = entity.getUserno() + "111111";;
    	String pwd = new PasswordEncoder("").encode(card);
    	login.setLoginpwd(pwd);
    	login.setType(entity.getOrgtype());// 1 机构 2 客户
    	login.setTelno(entity.getTelno());//同步手机号
    	login.setIsvalid("Y");
    	loginMapper.insert(login);
    	
//    	保存用户信息
    	entity.setPaypwd(pwd);
    	userMapper.insert(entity);
//    	保存客户关系
    	UserRelation ur = saveUserRelation(entity);
    	
    	//保存用户限额
    	if(entity.getOrgtype() == 2) {
    		saveUserLimit(entity,ur);
    	}
//    	保存角色
    	UserRole userRole = new UserRole();
		userRole.setLoginno(entity.getUserno());
    	if(entity.getOrgtype()==1) {
    		userRole.setRoleId(-1);
    	}else if(entity.getOrgtype()==2) {
    		userRole.setRoleId(-2);
    	}
		userRoleMapper.insert(userRole);
    	
		syncUser(entity);
    	redisUtils.hset(RedisKey.USER_INVITE_AUTH, entity.getUserno(), String.valueOf(entity.getUsertype()));
		return true;
	}
    
    @Override
    @Transactional(rollbackFor = Exception.class)
	public boolean updateUser(User entity)throws Exception  {
    	LoginUser loginUser = SecurityUtils.getLoginInfo();
    	if(loginUser.getType() > 0) {
    		if(StringUtils.isBlank(entity.getParentno())) {
    			throw new Exception("所属代理不能为空");
    		}
    		QueryWrapper<UserRelation> qw = new QueryWrapper<UserRelation>();
	   		 qw.eq("uno" + loginUser.getClevel(), loginUser.getUserno());
	   		 qw.eq("userno", entity.getUserno());
	   		 Integer count = userRelationMapper.selectCount(qw);
	   		 if(count <= 0) {
	   			 throw new Exception("暂无权限执行此操作");
	   		 }
    	}
//		判断用户是否存在
    	User user = userMapper.selectById(entity.getUserno());
    	if(user==null) {
    		throw new Exception("客户不存在");
    	}
    	if(!StringUtils.isBlank(entity.getOpenid())) {
    		User tjuser = userMapper.selectById(entity.getOpenid());
    		if(tjuser == null) {
    			throw new Exception("推荐用户不存在");
    		}
    	}
    	if(!user.getUsername().equals(entity.getUsername())) {
    		QueryWrapper<Login> qw = new QueryWrapper<Login>(); 
        	qw.eq("LOGINNO", entity.getUserno());
        	Login login = loginMapper.selectOne(qw);
        	if(login != null) {
        		login.setLoginname(entity.getUsername());
            	if(!user.getTelno().equals(entity.getTelno())){
            		login.setTelno(entity.getTelno());
            	}
            	loginMapper.updateById(login);
        	}
        	
    	}
    	userMapper.updateById(entity);
//    	保存客户关系
    	saveUserRelation(entity);
//    	保存角色
    	QueryWrapper<UserRole> qw = new QueryWrapper<>();
    	qw.eq("LOGINNO", entity.getUserno());
    	userRoleMapper.delete(qw);
//    	保存角色
    	UserRole userRole = new UserRole();
		userRole.setLoginno(entity.getUserno());
    	if(entity.getOrgtype()==1) {
    		userRole.setRoleId(-1);
    	}else if(entity.getOrgtype()==2) {
    		userRole.setRoleId(-2);
    	}
		userRoleMapper.insert(userRole);
    	 
    	//调整关系
//    	if(!user.getOpenid().equals(entity.getOpenid())) {
//    		QueryWrapper<UserRelation> qw = new QueryWrapper<UserRelation>();
//    		qw.like("tjno", user.getUserno());
//    		qw.or().eq("userno", user.getUserno());
//    		userRelationMapper.delete(qw);
//    		
//    		List<User> users = userMapper.findUnInitUser();
//	   		 Map<String,UserRelation> relationMap = new HashMap<String,UserRelation>();
//	   		 for(User item : users) {
//   				 UserRelation relation = getUserRelation(item,relationMap);
//   				 userRelationMapper.insert(relation);
//   				 log.info("初始化层级信息：【{}】-{}",relation.getUserno(),relation.getTjno());
//	   		}
//    	}
		
		entity.setSex("-1");
    	syncUser(entity);
    	redisUtils.hset(RedisKey.USER_INVITE_AUTH, entity.getUserno(), String.valueOf(entity.getUsertype()));
		return true;
	}
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(User entity) throws Exception {
    	LoginUser loginUser = SecurityUtils.getLoginInfo();
    	if(loginUser.getType() > 0) {
    		 QueryWrapper<UserRelation> qw = new QueryWrapper<UserRelation>();
    		 qw.eq("uno" + loginUser.getClevel(), loginUser.getUserno());
    		 qw.eq("userno", entity.getUserno());
    		 Integer count = userRelationMapper.selectCount(qw);
    		 if(count <= 0) {
    			 throw new Exception("暂无权限执行此操作");
    		 }
    	}
    	User item = userMapper.selectById(entity.getUserno());
    	item.setSex(entity.getSex());
    	userMapper.updateById(item);
    	syncUser(item);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUser(User entity)throws Exception  {
    	LoginUser loginUser = SecurityUtils.getLoginInfo();
    	if(loginUser.getType() > 0) {
    		 QueryWrapper<UserRelation> qw = new QueryWrapper<UserRelation>();
    		 qw.eq("uno" + loginUser.getClevel(), loginUser.getUserno());
    		 qw.eq("userno", entity.getUserno());
    		 Integer count = userRelationMapper.selectCount(qw);
    		 if(count <= 0) {
    			 throw new Exception("暂无权限执行此操作");
    		 }
    	}
//		判断用户是否存在
    	User user = userMapper.selectById(entity.getUserno());
    	if(user==null) {
    		throw new Exception("客户不存在");
    	}
//    	userMapper.deleteById(entity.getUserno());
//		判断用户是否存在
    	user.setIsvalid("N");
    	userMapper.updateById(user);
    	
//    	更新登陆账号
    	QueryWrapper<Login> qw = new QueryWrapper<Login>(); 
    	qw.eq("LOGINNO", entity.getUserno());
    	Login login = loginMapper.selectOne(qw);
    	if(login != null) {
    		login.setIsvalid("N");
        	loginMapper.updateById(login);
    	}
    	redisUtils.hdel(RedisKey.USER_INVITE_AUTH, entity.getUserno());
    	return true;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void clearMoney() throws Exception {
    	
    	userMapper.deleteMoney();
    	//userMapper.deleteChangeMoney();
    	//userMapper.deleteChangeMoneyApply();
    	
    	JSONObject data = new JSONObject();
		String ret = HttpClientUtils.doPost(BackCoreConfig.URL + BackCoreConfig.CLEAR_MONEY, data.toJSONString(),null);
		JSONObject json = JSONObject.parseObject(ret);
		if(!"0".equals(json.getString("iCode"))){
			throw new Exception(json.getString("sMsg"));
		}
    	
    }
    
    private void syncUser(User user) throws Exception {
    	JSONObject data = new JSONObject();
		data.put("userId", user.getUserno());
		data.put("name", user.getNickname());
		data.put("userName", user.getUsername());
		data.put("openid", user.getOpenid());
		data.put("sex", user.getSex());
		data.put("devType", 1);		
		data.put("parentno", user.getParentno());
		String ret = HttpClientUtils.doPost(BackCoreConfig.URL + BackCoreConfig.ADD_ACCOUNT, data.toJSONString(),null);
		JSONObject json = JSONObject.parseObject(ret);
		if(!"0".equals(json.getString("iCode"))){
			throw new Exception(json.getString("sMsg"));
		}
    }
    
    private UserRelation getUserRelation(User user,Map<String,UserRelation> relationMap) throws Exception {
    	StringBuffer buff = new StringBuffer();
    	UserRelation relation = new UserRelation();
    	relation.setUserno(user.getUserno());
    	relation.setUsername(user.getUsername());
    	if(StringUtils.isBlank(user.getOpenid())) {
    		relation.setClevel(1);
    	}else {
    		String openid = user.getOpenid();
    		while(!StringUtils.isBlank(openid)) {
    			if(buff.toString().indexOf(openid) > -1) {
    				throw new Exception(user.getUserno() + "层级设置重复");
    			}
    			buff.insert(0,"-" + openid);
    			UserRelation model = relationMap.get(openid);
    			if(model != null) {
    				buff.insert(0, "-" + model.getTjno());
    				openid = "";
    			}else {
        			User parent = userMapper.selectById(openid);
        			if(parent == null) break;
        			openid = parent.getOpenid();
    			}
    		}
    		String path = buff.toString();
    		if(path.length() > 0) {
    			path = path.substring(1);
    		}
    		String[] arr = path.split("-");
    		relation.setClevel(arr.length + 1);
    		if(arr.length >= 3) {
    			relation.setUno1(arr[0]);
    			relation.setUno2(arr[1]);
    			relation.setUno3(arr[2]);
    		}else if(arr.length ==2) {
    			relation.setUno1(arr[1]);
    			relation.setUno2(arr[0]);
    			relation.setUno3(arr[1]);
    		}else {
    			relation.setUno1(arr[0]);
    			relation.setUno2(arr[0]);
    			relation.setUno3(arr[0]);
    		}
    		relation.setTjno(path);
    		relationMap.put(relation.getUserno(), relation);
    	}
    	return relation;
    }
    
    
//  根据推荐人获取 层级关系
  private UserRelation saveUserRelation(User user)throws Exception{
  	UserRelation ur = new UserRelation();
  	
//  	删除历史数据
  	userRelationMapper.deleteById(user.getUserno());
  	if(StringUtils.isBlank(user.getParentno())) {//一级 
  		if(user.getClevel()==1) {
  			ur.setUno1(user.getUserno());
  		}else {
  			throw new Exception("请选择所属代理");
  		}
  	}else {
  		User parent = userMapper.selectById(user.getParentno());
  		if(parent == null) {
  			throw new Exception("所属代理不存在");
  		}
  		UserRelation parentur = userRelationMapper.selectById(parent.getUserno());
  		ur = parentur;
  		ur.setParentno(user.getParentno());
  		user.setClevel(ur.getClevel() + 1);
  		ur = setClevelVal(user.getClevel(), user.getUserno(), ur);
  	}
  	ur.setUserno(user.getUserno());
	ur.setUsername(user.getUsername());
	ur.setClevel(user.getClevel());
	ur.setTjno(user.getOpenid());

  	userRelationMapper.insert(ur);
  	
  	return ur;
  }
//  为曾级赋值
  private UserRelation setClevelVal(int level,String userno,UserRelation ur)throws Exception{
  	String fieldName = "uno" + level;
  	Field field = ur.getClass().getDeclaredField(fieldName);
  	 field.setAccessible(true);
  	 field.set(ur, userno);
  	 return ur;
  }
  
  @Override
	public List<Map<String, Object>> findOrgCount() throws Exception {
		LoginUser user = SecurityUtils.getLoginInfo();
		Map<String,Object> paraMap = new HashMap<String, Object>();
		if(user.getType() > 0) {
			paraMap.put(user.getQueryNo(), user.getUserno());
			paraMap.put("userno", user.getUserno());
			paraMap.put("clevel", user.getClevel());
		}
		return userMapper.findOrgNumByUser(paraMap);
	}
  
  @Override
  @Transactional(rollbackFor = Exception.class)
	public BigDecimal checkMoney() throws Exception {
		LoginUser user = SecurityUtils.getLoginInfo();
		if(user.getType() > 0) {
			String userno = user.getUserno();
			BigDecimal enableSelf = userMapper.findEnableSelf(userno);
			if(enableSelf == null) {
				enableSelf = new BigDecimal(0);
			}
			Map<String,Object> paraMap = new HashMap<String, Object>();
			paraMap.put("parentno", userno);
			Map<String, Object> data = userMapper.findUseMoney(paraMap);
			BigDecimal used = new BigDecimal(0);
			if(data != null) {
				used = new BigDecimal(data.get("enable").toString());
				enableSelf = enableSelf.subtract(used);
			}
			log.info("校验可用额度：【{}】：{}-{}",userno,enableSelf,used);
			return enableSelf;
		}else {
			return new BigDecimal(1);
		}
	}
  
  private void saveUserLimit(User user,UserRelation ur)throws Exception{
	  int clevel = ur.getClevel() - 1;
	  if(clevel <= 0) return;
	  List<String> usernos = new ArrayList<String>();
	  if(!StringUtils.isBlank(ur.getUno5())) {
		  usernos.add(ur.getUno5());
	  }
	  if(!StringUtils.isBlank(ur.getUno4())) {
		  usernos.add(ur.getUno4());
	  }
	  if(!StringUtils.isBlank(ur.getUno3())) {
		  usernos.add(ur.getUno3());
	  }
	  if(!StringUtils.isBlank(ur.getUno2())) {
		  usernos.add(ur.getUno2());
	  }
	  if(!StringUtils.isBlank(ur.getUno1())) {
		  usernos.add(ur.getUno1());
	  }
	  PageRequest request = new PageRequest();
	  request.setPageNum(1);
	  request.setPageSize(10);
	  Map<String,Object> params = new HashMap<String, Object>();
	  params.put("USERNO@IN", usernos);
	  params.put("CLEVEL@DESC", "A");
	  request.setParams(params);
	  
	  PageResult result = userLimitService.findPage(request);
	  if(result.getContent() != null) {
		  List<UserLimit> datas = (List<UserLimit>)result.getContent();
		  if(datas != null && datas.size() > 0) {
			  UserLimit limit = datas.get(0);
			  limit.setUserno(user.getUserno());
			  userLimitService.save(limit);
		  }
	  }
  }
}

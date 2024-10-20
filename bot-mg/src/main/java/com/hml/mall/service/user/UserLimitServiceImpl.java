package com.hml.mall.service.user;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hml.core.page.MybatisPlusPageHelper;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.user.User;
import com.hml.mall.entity.user.UserLimit;
import com.hml.mall.entity.user.UserRelation;
import com.hml.mall.iface.user.IUserLimitService;
import com.hml.mall.mapper.user.UserLimitMapper;
import com.hml.mall.mapper.user.UserMapper;
import com.hml.mall.mapper.user.UserRelationMapper;
import com.hml.redis.RedisUtils;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hml
 * @since 2024-09-19
 */

@Service("userLimitService")
public class UserLimitServiceImpl extends ServiceImpl<UserLimitMapper, UserLimit> implements IUserLimitService {

	@Autowired
	private UserLimitMapper userLimitMapper;
	
	@Autowired
	private UserRelationMapper userRelationMapper;
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private RedisUtils redisUtils;
	
	private String REDIS_KEY = "USER_LIMIT_AUTH:";

    @Override
    public List< UserLimit> list(UserLimit model) {
       QueryWrapper< UserLimit> wrapper = new QueryWrapper();
       return super.list(wrapper);
    }

    @Override
	public PageResult findPage(PageRequest pageRequest) {
		 
		return MybatisPlusPageHelper.findPage(pageRequest, userLimitMapper,"findPage");
	}

    @Override
    @Transactional(rollbackFor = Exception.class)
	public boolean save(UserLimit entity) {
    	 User user = userMapper.selectById(entity.getUserno());
    	 if(user == null) {
    		 throw new RuntimeException(entity.getUserno() + "不存在");
    	 }
    	 UserLimit item = userLimitMapper.selectById(entity.getUserno());
    	 if(item != null) {
    		 throw new RuntimeException(entity.getUserno() + "已存在");
    	 }
		 boolean flag = super.save(entity);
		 
    	 if(user.getOrgtype() == 1) {
    		 UserRelation relation = userRelationMapper.selectById(entity.getUserno());
    		 if(!checkMaxLimit(relation.getParentno(),entity)){
    			 throw new RuntimeException("最大限额不能超过上级设置最大限额");
    		 }
    		 QueryWrapper<UserLimit> qw = new QueryWrapper<UserLimit>();
    		 qw.eq("uno" + relation.getClevel(), entity.getUserno());
    		 qw.eq("orgtype", 2);
    		 List<UserRelation> items = userLimitMapper.findUser(qw);
    		 for(UserRelation temp : items) {
    			 userLimitMapper.deleteById(temp.getUserno());
    			 entity.setUserno(temp.getUserno());
    			 super.save(entity);
    			 redisUtils.del(REDIS_KEY + entity.getUserno());
    		 }
    	 }else {
    		 redisUtils.del(REDIS_KEY + entity.getUserno());
    	 }
//		 redisUtils.set(REDIS_KEY + entity.getUserno(),  JSONObject.toJSONString(entity));
		 return flag;
	}
    
    @Override
    @Transactional(rollbackFor = Exception.class)
	public boolean updateById(UserLimit entity) {
		 boolean flag = super.updateById(entity);
		 User user = userMapper.selectById(entity.getUserno());
		 if(user.getOrgtype() == 1) {
    		 UserRelation relation = userRelationMapper.selectById(entity.getUserno());
    		 if(!checkMaxLimit(relation.getParentno(),entity)){
    			 throw new RuntimeException("最大限额不能超过上级设置最大限额");
    		 }
    		 QueryWrapper<UserLimit> qw = new QueryWrapper<UserLimit>();
    		 qw.eq("uno" + relation.getClevel(), entity.getUserno());
    		 qw.eq("orgtype", 2);
    		 List<UserRelation> items = userLimitMapper.findUser(qw);
    		 for(UserRelation temp : items) {
    			 userLimitMapper.deleteById(temp.getUserno());
    			 entity.setUserno(temp.getUserno());
    			 super.save(entity);
    			 redisUtils.del(REDIS_KEY + entity.getUserno());
    		 }
    	 }else {
    		 redisUtils.del(REDIS_KEY + entity.getUserno());
    	 }
//		 redisUtils.set(REDIS_KEY + entity.getUserno(),  JSONObject.toJSONString(entity));
		 return flag;
	}
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeById(Serializable id) {
    	 boolean flag = super.removeById(id);
    	 User user = userMapper.selectById(id);
		 if(user.getOrgtype() == 1) {
    		 UserRelation relation = userRelationMapper.selectById(id);
    		 QueryWrapper<UserLimit> qw = new QueryWrapper<UserLimit>();
    		 qw.eq("uno" + relation.getClevel(), id);
    		 qw.eq("orgtype", 2);
    		 List<UserRelation> items = userLimitMapper.findUser(qw);
    		 for(UserRelation temp : items) {
    			 userLimitMapper.deleteById(temp.getUserno());
    			 redisUtils.del(REDIS_KEY + temp.getUserno());
    		 }
    	 }else {
    		 redisUtils.del(REDIS_KEY + id);
    	 }
    	return flag;
    }
    
    private boolean checkMaxLimit(String userno,UserLimit entity) {
    	boolean flag = true;
    	if(userno == null) return flag;
    	UserLimit parent = userLimitMapper.selectById(userno);
    	
    	if(parent == null) return flag;
    	
    	if((parent.getMaxDou() != 0 && parent.getMaxDou() < entity.getMaxDou())
    			|| (parent.getMaxNiuP() != 0 && parent.getMaxNiuP() < entity.getMaxNiuP())
    			|| (parent.getMaxNiuB() != 0 && parent.getMaxNiuB() < entity.getMaxNiuB())) {
    		flag = false;
    		return flag;
    	}
    	 
    	return flag;
    }
}

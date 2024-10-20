package com.hml.mall.service.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hml.core.page.MybatisPlusPageHelper;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.user.UserChange;
import com.hml.mall.entity.user.UserChangeDtl;
import com.hml.mall.iface.user.IUserChangeService;
import com.hml.mall.mapper.user.UserChangeMapper;
import com.hml.redis.RedisUtils;
import com.hml.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hml
 * @since 2021-06-29
 */

@Service("userChangeService")
@Slf4j
public class UserChangeServiceImpl extends ServiceImpl<UserChangeMapper, UserChange> implements IUserChangeService {

	@Autowired
	private UserChangeMapper userChangeMapper;
	
	@Autowired
	private RedisUtils redisUtils;

    @Override
    public List< UserChange> list(UserChange model) {
       QueryWrapper< UserChange> wrapper = new QueryWrapper();
       return super.list(wrapper);
    }

    @Override
	public PageResult findPage(PageRequest pageRequest) {
		 
		return MybatisPlusPageHelper.findPage(pageRequest, userChangeMapper,"findPage");
	}

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditUser(UserChange model) throws Exception {
    	
    	  UserChange userChange = userChangeMapper.selectById(model.getWno());
//    	  if(!"1".equals(userChange.getCheckstatus())) {//未审核
//    		  throw new Exception("数据状态不正确，暂不能审核！");
//    	  }
//    	  审核
    	  if("2".equals(model.getCheckstatus())) {
    		  userChange.setCheckstatus("2");
    		  if(StringUtils.isBlank(userChange.getChcontent())) {
    			  throw new Exception("申请变更内容不能为空！");
    		  }
    		  UserChangeDtl dtl = new UserChangeDtl(userChange.getUserno(), userChange.getChcontent());
//    		  实名认证
    		  if("1".equals(userChange.getChtype())) {
    			  dtl.setREALSTATUS("2");
    			  userChangeMapper.updateUserReal(dtl);
    		  }else if("2".equals(userChange.getChtype())) {//VIP等级
    			  userChangeMapper.updateUserVip(dtl);
    		  }
    	  }else if("3".equals(model.getCheckstatus())) {//驳回
    		  userChange.setCheckstatus("3");
    		  userChange.setCheckmsg(model.getCheckmsg());
    		  UserChangeDtl dtl = new UserChangeDtl(userChange.getUserno(), userChange.getChcontent());
//    		   实名认证驳回
    		  if("1".equals(userChange.getChtype())) {
    			  dtl.setREALSTATUS("3");
    			  userChangeMapper.updateUserReal(dtl);
    		  }
    	  }else {
    		  throw new Exception("未知业务类型");
    	  }
    	  userChange.setChkip(model.getChkip());
		  userChange.setChkuser(model.getChkuser());
		  userChange.setChktime(model.getChktime());
		  userChangeMapper.updateById(userChange);
		  
		  redisUtils.del("tb_hy_user#"+userChange.getUserno());
		  redisUtils.del("tb_hy_user_query#"+userChange.getUserno());
		  log.info("实名认证审核驳回清除redis数据：{}",userChange.getUserno() );
    }
}

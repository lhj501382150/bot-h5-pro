package com.hml.mall.service.game;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hml.core.http.HttpResult;
import com.hml.core.page.MybatisPlusPageHelper;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.game.Activity;
import com.hml.mall.entity.game.HorseRule;
import com.hml.mall.entity.game.Rewar;
import com.hml.mall.entity.user.UserRelation;
import com.hml.mall.iface.game.IActivityService;
import com.hml.mall.mapper.game.ActivityMapper;
import com.hml.mall.mapper.game.HorseRuleMapper;
import com.hml.mall.mapper.game.RewarMapper;
import com.hml.mall.security.LoginUser;
import com.hml.mall.util.SecurityUtils;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hml
 * @since 2021-06-09
 */

@Service("activityService")
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity> implements IActivityService {

	@Autowired
	private ActivityMapper activityMapper;
	
	@Autowired
	private RewarMapper rewarMapper;
	
	@Autowired
	private HorseRuleMapper horseRuleMapper;

    @Override
    public List< Activity> list(Activity model) {
       QueryWrapper< Activity> wrapper = new QueryWrapper();
       LoginUser loginUser = SecurityUtils.getLoginInfo();
	   	if(loginUser.getType() > 0){
	   		String key = loginUser.getQueryNo();
	   		wrapper.eq(key, loginUser.getUserno());
	   	}
       return activityMapper.findList(wrapper);
    }

    @Override
	public PageResult findPage(PageRequest pageRequest) {
    	LoginUser loginUser = SecurityUtils.getLoginInfo();
    	if(loginUser.getType() > 0){
    		String key = loginUser.getQueryNo();
    		 pageRequest.getParams().put(key, loginUser.getUserno());
    	}
		return MybatisPlusPageHelper.findPage(pageRequest, activityMapper,"findPage");
	}
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveLike(Activity model) throws Exception {
    	Long actNo = model.getActno();
    	model.setActno(null);
    	model.setAstatus(0);
    	activityMapper.insert(model);
    	Long newActNo = model.getActno();
//    	保存奖励设置
    	QueryWrapper<Rewar> qw = new QueryWrapper<Rewar>();
    	qw.eq("ACTNO", actNo);
    	List<Rewar> rewarList = rewarMapper.selectList(qw);
    	for(Rewar item: rewarList) {
    		item.setActno(newActNo);
    		rewarMapper.insert(item);
    	}
//    	保存规则设置
    	QueryWrapper<HorseRule> qw2 = new QueryWrapper<HorseRule>();
    	qw2.eq("ACTNO", actNo);
    	List<HorseRule> ruleList = horseRuleMapper.selectList(qw2);
    	for(HorseRule item: ruleList) {
    		item = item.clone(newActNo);
    		horseRuleMapper.insert(item);
    	}
    	
    }

}

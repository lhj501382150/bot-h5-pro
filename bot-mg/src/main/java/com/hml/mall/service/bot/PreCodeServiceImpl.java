package com.hml.mall.service.bot;

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
import com.hml.mall.entity.bot.PreCode;
import com.hml.mall.iface.bot.IPreCodeService;
import com.hml.mall.mapper.bot.PreCodeMapper;
import com.hml.redis.RedisKey;
import com.hml.redis.RedisUtils;
import com.hml.utils.DateTimeUtils;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hml
 * @since 2024-11-03
 */

@Service("preCodeService")
public class PreCodeServiceImpl extends ServiceImpl<PreCodeMapper, PreCode> implements IPreCodeService {

	@Autowired
	private PreCodeMapper preCodeMapper;
	
	@Autowired
	private RedisUtils redisUtils;

    @Override
    public List< PreCode> list(PreCode model) {
       QueryWrapper< PreCode> wrapper = new QueryWrapper();
       return super.list(wrapper);
    }

    @Override
	public PageResult findPage(PageRequest pageRequest) {
		 
		return MybatisPlusPageHelper.findPage(pageRequest, preCodeMapper);
	}

    @Override
    @Transactional(rollbackFor = Exception.class)
	public boolean save(PreCode entity) {
    	String mode = entity.getRkey();
    	String issue = getDrawIssue(mode);
    	QueryWrapper<PreCode> qw = new QueryWrapper<PreCode>();
    	qw.eq("MODE", mode);
    	qw.eq("DRAW_ISSUE", issue);
    	Integer count = preCodeMapper.selectCount(qw);
    	if(count > 0) {
    		throw new RuntimeException(issue + "期：行情已存在");
    	}
    	entity.setMode(getMode(mode));
		entity.setDataId(getDrawId(mode));
		entity.setDrawIssue(issue);
		boolean flag = super.save(entity);
		setRedisResult(mode, entity.getCode());
		return flag;
	}
    
    @Override
    @Transactional(rollbackFor = Exception.class)
	public boolean updateById(PreCode entity) {
    	String mode = entity.getRkey();
		long drawId = getDrawId(mode);
		if(drawId != entity.getDataId()) {
			throw new RuntimeException("该期结果已生效，不能修改");
		}
		boolean flag =  super.updateById(entity);
		setRedisResult(mode, entity.getCode());
		return flag;
	}
    
    
    private long getDrawId(String key) {
		key = RedisKey.DRAW_ID + key;
		Object obj = redisUtils.get(key);
		if(obj == null) {
			throw new RuntimeException("期数不存在");
		}
		long id = Long.valueOf(obj.toString()) + 1;
		return id;
	}
	/*
	 * 获取开奖号
	 */
	private String getDrawIssue(String key) {
		String currentDate = DateTimeUtils.getCurrentDate("yyMMdd");
		key = RedisKey.DRAW_ISSUE + key + ":" + currentDate;
		Object obj = redisUtils.get(key);
		String val = "";
		if(obj == null) {
			throw new RuntimeException("期数不存在");
		}else {
			val = obj.toString();
		}
		long issue = Long.valueOf(val) + 1;
		return String.valueOf(issue);
	}
	
	private void setRedisResult(String key,String code) {
		String redisKey = RedisKey.DRAW_RESULT + key;
		redisUtils.set(redisKey, code);
	}
	
	private Integer getMode(String key) {
		Map<String,Integer> map = new HashMap<>();
		map.put("HXBD1", 2);
		map.put("HXBD3", 3);
		map.put("HXBD5", 4);
		map.put("HXNN1", 5);
		map.put("HXNN3", 6);
		map.put("HXNN5", 7);
		
		return map.get(key);
	}
}

package com.hml.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.hml.redis.Redis2Utils;
import com.hml.redis.RedisKey;
import com.hml.redis.RedisUtils;
import com.hml.task.SysTaskManager;

@Component
public class InitDataRunner implements ApplicationRunner {

	@Autowired
	private RedisUtils redisUtils;
	@Autowired
	private Redis2Utils redis2Utils;
	
	@Autowired
	private SysTaskManager sysTaskManager;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
	
		redisUtils.del(RedisKey.ORDER_QUERY,RedisKey.CURRENT_STATUS);
		redis2Utils.del(RedisKey.ORDER_QUERY,RedisKey.CURRENT_STATUS);
		redisUtils.del(RedisKey.ORDER_QUERY_MODE + 2);
		redisUtils.del(RedisKey.ORDER_QUERY_MODE + 3);
		redisUtils.del(RedisKey.ORDER_QUERY_MODE + 4);
		redisUtils.del(RedisKey.ORDER_QUERY_MODE + 5);
		redisUtils.del(RedisKey.ORDER_QUERY_MODE + 6);
		redisUtils.del(RedisKey.ORDER_QUERY_MODE + 7);
		sysTaskManager.checkSysAuth();
	}

}

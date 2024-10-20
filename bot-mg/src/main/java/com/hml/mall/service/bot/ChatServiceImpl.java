package com.hml.mall.service.bot;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties.Redis;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hml.backcore.config.BackCoreConfig;
import com.hml.backcore.service.BackCoreService;
import com.hml.core.page.MybatisPlusPageHelper;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.bot.Chat;
import com.hml.mall.entity.sys.Para;
import com.hml.mall.entity.user.User;
import com.hml.mall.iface.bot.IChatService;
import com.hml.mall.mapper.bot.ChatMapper;
import com.hml.mall.mapper.sys.ParaMapper;
import com.hml.mall.mapper.user.UserMapper;
import com.hml.redis.RedisKey;
import com.hml.redis.RedisUtils;
import com.hml.utils.HttpClientUtils;
import com.hml.utils.StringUtils;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hml
 * @since 2023-12-08
 */

@Service("chatService")
public class ChatServiceImpl extends ServiceImpl<ChatMapper, Chat> implements IChatService {

	@Autowired
	private ChatMapper chatMapper;
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private BackCoreService backCoreService;
	
	@Autowired
	private RedisUtils redisUtils;
	
	@Autowired
	private ParaMapper paraMapper;

    @Override
    public List< Chat> list(Chat model) {
       QueryWrapper< Chat> wrapper = new QueryWrapper();
       return super.list(wrapper);
    }

    @Override
	public PageResult findPage(PageRequest pageRequest) {
		 
		return MybatisPlusPageHelper.findPage(pageRequest, chatMapper,"findPage");
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public void down(Chat item) {
		Chat model = chatMapper.selectById(item.getChatid());
		if(model != null && !StringUtils.isBlank(model.getUserno())) {
			
			User user = userMapper.selectById(model.getUserno());
			if(user == null){
				throw new RuntimeException("庄家信息缺失");
			}
			JSONObject req = new JSONObject();
			req.put("botId", String.valueOf(model.getChatid()));
			req.put("dataId", 0);
			req.put("userId", model.getUserno());
			req.put("userName", user.getUsername()); 
			String res = backCoreService.downRob(req);
			if("ok".equals(res)){
				model.setUserno("");
				chatMapper.updateById(model);
				
				redisUtils.set(RedisKey.ROB_DOWN + model.getChatid(), "A",30);
			}else{
				throw new RuntimeException(res);
			}
		}else{
			throw new RuntimeException("当前不需要下庄");
		}
		
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public void editStatus(Chat item) {
		redisUtils.set(RedisKey.ROB_STATUS + item.getChatid(), item.getStatus());
		chatMapper.updateById(item);
	}
	
	@Override
	public boolean updateById(Chat item) {
		Chat chat = chatMapper.selectById(item.getChatid());
		
		Para minPara = paraMapper.selectById(3);
		Para maxPara = paraMapper.selectById(4);
		
		if(chat == null) {
			throw new RuntimeException("请选择 正确群设置");
		}
		int min = 0,max = 0;
				
		try {
			min = Integer.parseInt(minPara.getSval());
			max = Integer.parseInt(maxPara.getSval());
			
		} catch (Exception e) {
			throw new RuntimeException("抢庄金额参数设置异常");
		}
		if(item.getIntege() < min || item.getIntege() > max) {
			throw new RuntimeException("抢庄金额应在" + min + "-" + max+ "之间");
		}
		
		chat.setIntege(item.getIntege());
		boolean flag = super.updateById(chat);
		
		JSONObject data = new JSONObject();
		data.put("botId", chat.getChatid());
		data.put("intege", chat.getIntege());
		try {
			String ret = HttpClientUtils.doPost(BackCoreConfig.URL + BackCoreConfig.ALTER_BOT_INTEGE, data.toJSONString(),null);
			JSONObject json = JSONObject.parseObject(ret);
			if(!"0".equals(json.getString("iCode"))){
				throw new RuntimeException(json.getString("sMsg"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return flag;
	}

}

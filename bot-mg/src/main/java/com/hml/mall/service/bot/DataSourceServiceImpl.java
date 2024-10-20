package com.hml.mall.service.bot;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hml.backcore.service.BackCoreService;
import com.hml.core.page.MybatisPlusPageHelper;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.bot.DataSource;
import com.hml.mall.mapper.bot.DataSourceMapper;
import com.hml.redis.RedisKey;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hml
 * @since 2023-12-14
 */

@Service("DataSourceService")
public class DataSourceServiceImpl extends ServiceImpl<DataSourceMapper, DataSource> implements IDataSourceService {

	@Autowired
	private DataSourceMapper dataSourceMapper;
	
	@Autowired
    private BackCoreService backCoreService;

    @Override
    public List< DataSource> list(DataSource model) {
       QueryWrapper< DataSource> wrapper = new QueryWrapper();
       return super.list(wrapper);
    }

    @Override
	public PageResult findPage(PageRequest pageRequest) {
		 
//		return MybatisPlusPageHelper.findPage(pageRequest, DataSourceMapper,"findPage");
		
		return MybatisPlusPageHelper.findPage(pageRequest, dataSourceMapper);
	}
    
    @Override
    public Integer findPreDataSource(String issue,int mode) {
    	DataSource item = dataSourceMapper.findNearDataSorce(issue,mode);
    	if(item == null) {
    		throw new RuntimeException("未找到上一期结果");
    	}
    	int preIssue = Integer.parseInt(item.getIssue());
    	int curIssue = Integer.parseInt(issue);
    	int dataId = curIssue - preIssue + item.getDataId();
    	return dataId;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(DataSource entity) {
    	JSONObject data = new JSONObject();
    	data.put("id",entity.getDataId());
    	data.put("BotId", Long.valueOf(entity.getWareno()));
    	data.put("drawIssue", Integer.parseInt(entity.getIssue()));
    	data.put("sTime", entity.getStime());
    	data.put("sResult", entity.getSresult());
    	data.put("Mode", entity.getMode());
    	String res = backCoreService.addManaData(data);
    	if("ok".equals(res)){
    		return true;
		}else{
			throw new RuntimeException(res);
		}
    }

}

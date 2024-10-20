package com.hml.mall.service.sys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
import com.hml.mall.entity.sys.Para;
import com.hml.mall.iface.sys.IParaService;
import com.hml.mall.mapper.sys.ParaMapper;
import com.hml.utils.HttpClientUtils;
import com.hml.utils.StringUtils;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hml
 * @since 2021-04-05
 */

@Service("paraService")
public class ParaServiceImpl extends ServiceImpl<ParaMapper, Para> implements IParaService {

	@Autowired
	private ParaMapper paraMapper;

    @Override
    public List<Map<String,Object>> list(Para model) {
       QueryWrapper<Para> wrapper = new QueryWrapper();
       wrapper.orderByAsc("SYSTYPE");
       if(model.getSysid()!=null){
    	  wrapper.eq("SYSID", model.getSysid()); 
       }
       if(!StringUtils.isBlank(model.getSysname())){
    	   wrapper.like("SYSNAME", model.getSysname());
       }
       List<Para> list = paraMapper.selectList(wrapper);
       Map<String,List<Para>> map = new LinkedHashMap<>();
       for(Para para:list){
    	   List<Para> child = null;
    	   String type = para.getSystype();
    	   String name = para.getGroupname();
    	   String key = type + "@" + name; 
    	   child = map.get(key);
    	   if(child == null){
    		   child = new ArrayList<Para>();
    	   }
    	   child.add(para);
    	   map.put(key, child);
       }
       List<Map<String,Object>> data = new ArrayList<>();
       for(String key:map.keySet()){
    	   String[] s = key.split("@");
    	   Map<String,Object> temp =new HashMap<>();
    	   temp.put("sysid", s[0]);
    	   temp.put("systype", s[0]);
    	   temp.put("groupname", s[1]);
    	   temp.put("isvalid","Y");
    	   temp.put("children", map.get(key));
    	   data.add(temp);
       }
       return data;
    }

    @Override
    public PageResult findPage(PageRequest pageRequest) {
    	 
    	return MybatisPlusPageHelper.findPage(pageRequest, paraMapper);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
	public boolean updateById(Para entity) {
		
		 boolean flag = super.updateById(entity);
		 JSONObject data = new JSONObject();
		data.put("sysId", String.valueOf(entity.getSysid()));
		data.put("sVal", entity.getSval());
		try {
			String ret = HttpClientUtils.doPost(BackCoreConfig.URL + BackCoreConfig.ALTER_PARA, data.toJSONString(),null);
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

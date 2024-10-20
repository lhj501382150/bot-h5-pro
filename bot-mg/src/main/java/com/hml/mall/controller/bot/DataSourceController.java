package com.hml.mall.controller.bot;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hml.backcore.service.BackCoreService;
import com.hml.core.http.HttpResult;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.bot.DataSource;
import com.hml.mall.service.bot.IDataSourceService;
import com.hml.redis.RedisKey;
import com.hml.redis.RedisUtils;
import com.hml.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;


/**
*  前端控制器
* @author hml
* @since 2023-12-14
*/

@Slf4j
@RestController
@RequestMapping("/dataSource" )
public class DataSourceController {

    @Autowired
    private IDataSourceService  dataSourceService;
    
    @Autowired
    private RedisUtils redisUtils;

//    /**
//    * 保存
//    *
//    * @param model
//    * @return
//    */
//    @PreAuthorize("hasAuthority('operations:draw:add')")
//    @RequestMapping("/save")
//    public HttpResult save(@RequestBody DataSource model) {
//        DataSourceService.save(model);
//        return HttpResult.ok();
//    }
 	/**
    * 修改
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('operations:draw:edit')")
    @RequestMapping("/edit")
    public HttpResult edit(@RequestBody DataSource model) {
    	String key  = RedisKey.ROB_SEND_PIC + model.getWareno();
    	
    	redisUtils.lSet(key, model.getContnum());
    	
    	log.info("重新发送图片：{}-{}", key, model.getContnum() );
        return HttpResult.ok();
    }
    @PreAuthorize("hasAuthority('operations:draw:add')")
    @RequestMapping("/save")
    public HttpResult save(@RequestBody DataSource model) throws Exception {
    	if(StringUtils.isBlank(model.getMode())) {
    		throw new Exception("类型不能为空");
    	}
    	if(StringUtils.isBlank(model.getIssue())) {
    		throw new Exception("期数不能为空");
    	}
    	if(StringUtils.isBlank(model.getSresult())) {
    		throw new Exception("开奖结果不能为空");
    	}
    	log.info("补录开奖结果：{}",model);
    	String issue = model.getIssue();
    	QueryWrapper<DataSource> qw = new QueryWrapper<DataSource>();
    	qw.eq("issue",issue);
    	qw.eq("mode", model.getMode());
    	DataSource item = dataSourceService.getOne(qw);
    	if(item != null) {
    		throw new Exception("该期结果已存在");
    	}
//    	查询ID
    	Integer id = dataSourceService.findPreDataSource(issue,model.getMode());
    	model.setDataId(id);
    	model.setSresult(checkResult(model.getSresult()));
    	dataSourceService.save(model);
        return HttpResult.ok();
    }
	/**
    * 删除
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('operations:draw:del')")
    @RequestMapping("/delete")
    public HttpResult delete(@RequestBody DataSource model) {
    	
        return HttpResult.ok();
    }
    /**
    * 查询详情
    *
    * @param id
    * @return
    */
    @PreAuthorize("hasAuthority('operations:draw:view')")
    @RequestMapping("/getById")
    public HttpResult getById(Integer id) {
     DataSource model = dataSourceService.getById(id);
        // todo 再包装一层
        return HttpResult.ok(model);
    }


    /**
    * 列表查询（非分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('operations:draw:view')")
    @RequestMapping("/list")
    public HttpResult list() {
        DataSource model = new  DataSource();
        List<DataSource> list =  dataSourceService.list(model);
        // todo 再包装一层
        return HttpResult.ok(list);
    }


    /**
    * 列表查询（分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('operations:draw:view')")
    @RequestMapping("/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {

    	pageRequest.getParams().put("sTime@desc", "1");
        PageResult page = dataSourceService.findPage(pageRequest);
        // todo 再包装一层
        return HttpResult.ok(page);
    }

    private String checkResult(String result) throws Exception {
    	result = result.replaceAll("，", ",");
    	String[] rets = result.split(",");
    	if(rets.length != 10) {
    		throw new Exception("开奖结果应为10位数字");
    	}
    	StringBuffer buff = new StringBuffer();
    	for(int i=0;i < rets.length;i++) {
    		String num = rets[i].trim();
    		if(StringUtils.isBlank(num)) {
    			throw new Exception("开奖结果数字不能为空");
    		}
    		int m = Integer.parseInt(num);
    		if(m < 10) {
    			buff.append("0").append(m).append(",");
    		}else {
    			buff.append(m).append(",");
    		}
    	}
    	String str = buff.toString();
    	if(str.length() > 0) {
    		str = str.substring(0,str.length() -1);
    	}
    	return str;
    	
    }
    
     
}

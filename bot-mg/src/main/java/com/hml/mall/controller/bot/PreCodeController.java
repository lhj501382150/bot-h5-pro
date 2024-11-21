package com.hml.mall.controller.bot;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.hml.core.http.HttpResult;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.bot.PreCode;
import com.hml.mall.entity.order.Order;
import com.hml.mall.iface.bot.IPreCodeService;
import com.hml.mall.util.SecurityUtils;
import com.hml.redis.RedisUtils;
import com.hml.utils.DateTimeUtils;


/**
*  前端控制器
* @author hml
* @since 2024-11-03
*/


@RestController
@RequestMapping("/preCode" )
public class PreCodeController {

    @Autowired
    private IPreCodeService  preCodeService;
    @Autowired
    private RedisUtils redisUitls;

    /**
    * 保存
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('operations:preCode:add')")
    @RequestMapping("/save")
    public HttpResult save(@RequestBody PreCode model) {
    	String curDate = DateTimeUtils.getDateTime();
    	if(curDate.compareTo(model.getDrawTime()) > 0) {
    		return HttpResult.error("该期已开奖");
    	}
    	model.setCreateTime(LocalDateTime.now());
    	model.setCreateUser(SecurityUtils.getUsername());
        preCodeService.save(model);
        return HttpResult.ok();
    }
 	/**
    * 修改
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('operations:preCode:edit')")
    @RequestMapping("/edit")
    public HttpResult edit(@RequestBody PreCode model) {
    	model.setUpdateTime(LocalDateTime.now());
    	model.setUpdateUser(SecurityUtils.getUsername());
        preCodeService.updateById(model);
        return HttpResult.ok();
    }
	/**
    * 删除
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('operations:preCode:del')")
    @RequestMapping("/delete")
    public HttpResult delete(@RequestBody PreCode model) {
        preCodeService.removeById(model);
        return HttpResult.ok();
    }
    /**
    * 查询详情
    *
    * @param id
    * @return
    */
    @PreAuthorize("hasAuthority('operations:preCode:view')")
    @RequestMapping("/getDraw")
    public HttpResult getDraw(@RequestBody PreCode model) {
    	String redisKey = "MODE_HQ_RESULT:" + model.getRkey();
    	
    	Object obj = redisUitls.get(redisKey);
    	if(obj == null) {
    		return HttpResult.error("未加载行情结果");
    	}
    	PreCodeData draw = JSONObject.parseObject(obj.toString(), PreCodeData.class);
    	 
    	String contnum = String.valueOf(draw.getId() + 1);
    	String mode = draw.getMode();
    	
    	List<Order> orders = preCodeService.findDraw(mode,contnum);
    	Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();	
    	 if("2".equals(mode) || "3".equals(mode) || "4".equals(mode)) {//宝斗
    		 for(Order item:orders) {
    			 String key = item.getArtid();
    			 key = key.substring(0,1);
    			 BigDecimal total = map.get(key);
    			 if(total == null) {
    				 total = item.getBailmoney();
    			 }else {
    				 total = total.add(item.getBailmoney());
    			 }
    			 map.put(key, total);
    		 }
    	 }else {//牛牛
    		 
    	 }
         
    	 Map<String,Object> data = new HashMap<String, Object>();
    	 data.put("draw", draw);
    	 data.put("orders", orders);
    	 data.put("count", map);
    	 
        return HttpResult.ok(data);
    }


    /**
    * 列表查询（非分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('operations:preCode:view')")
    @RequestMapping("/list")
    public HttpResult list() {
        PreCode model = new  PreCode();
        List<PreCode> list =  preCodeService.list(model);
        // todo 再包装一层
        return HttpResult.ok(list);
    }


    /**
    * 列表查询（分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('operations:preCode:view')")
    @RequestMapping("/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {
    	pageRequest.getParams().put("id@desc", "a");
        PageResult page = preCodeService.findPage(pageRequest);
        
        return HttpResult.ok(page);
    }

}

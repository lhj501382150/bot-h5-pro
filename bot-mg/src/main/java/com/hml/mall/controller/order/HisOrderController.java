package com.hml.mall.controller.order;


import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hml.core.http.HttpResult;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.iface.order.IHisOrderService;
import com.hml.utils.StringUtils;


/**
*  前端控制器
* @author hml
* @since 2021-04-18
*/


@RestController
@RequestMapping("/hisOrder" )
public class HisOrderController {

    @Autowired
    private IHisOrderService  hisOrderService;
    
	/**
    * 删除
    *
    * @param model
    * @return
	 * @throws Exception 
    */
    @PreAuthorize("hasAuthority('order:his:del')")
    @RequestMapping("/delete")
    public HttpResult delete(@RequestBody Map<String,String> paraMap) throws Exception {
        if(StringUtils.isBlank(paraMap.get("bdate"))) {
        	return HttpResult.error("开始日期不能为空");
        }
        if(StringUtils.isBlank(paraMap.get("edate"))) {
        	return HttpResult.error("结束日期不能为空");
        }
        hisOrderService.deleteHisData(paraMap.get("bdate"),paraMap.get("edate"));
        
        return HttpResult.ok();
    }
     


    /**
    * 列表查询（分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('order:his:view')")
    @RequestMapping("/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {

        PageResult page = hisOrderService.findPage(pageRequest);
        // todo 再包装一层
        return HttpResult.ok(page);
    }
    
    @PreAuthorize("hasAuthority('hisquery:money:view')")
    @RequestMapping("/findScorePage")
    public HttpResult findScorePage(@RequestBody PageRequest pageRequest) {

    	pageRequest.getParams().put("SUBNO@IN", Arrays.asList("100","101"));
        PageResult page = hisOrderService.findScorePage(pageRequest);
        // todo 再包装一层
        return HttpResult.ok(page);
    }
    
    @RequestMapping("/findCount")
    public HttpResult findCount(@RequestBody PageRequest pageRequest) {
    	
    	PageResult page = hisOrderService.findCount(pageRequest);
    	return HttpResult.ok(page);
    }
    
    @RequestMapping("/findLevelCount")
    public HttpResult findLevelCount(@RequestBody PageRequest pageRequest) throws Exception {
    	
    	PageResult page = hisOrderService.findUserLevelCount(pageRequest);
    	return HttpResult.ok(page);
    }
    
    @RequestMapping("/findFYCount")
    public HttpResult findFYCount(@RequestBody PageRequest pageRequest) {
    	
    	PageResult page = hisOrderService.findFYCount(pageRequest);
    	// todo 再包装一层
    	return HttpResult.ok(page);
    }

}

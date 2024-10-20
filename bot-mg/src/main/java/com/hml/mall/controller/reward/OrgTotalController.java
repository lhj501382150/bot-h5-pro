package com.hml.mall.controller.reward;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hml.core.http.HttpResult;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.reward.OrgTotal;
import com.hml.mall.iface.reward.IOrgTotalService;
import com.hml.utils.StringUtils;


/**
*  前端控制器
* @author hml
* @since 2021-05-26
*/


@RestController
@RequestMapping("/orgTotal" )
public class OrgTotalController {

    @Autowired
    private IOrgTotalService  orgTotalService;

    /**
    * 保存
    *
    * @param model
    * @return
     * @throws Exception 
    */
    @PreAuthorize("hasAuthority('reward:orgtotal:add')")
    @RequestMapping("/count")
    public HttpResult count(@RequestBody Map<String,Object> map) throws Exception {
    	if(StringUtils.isBlank(map.get("btime"))) {
    		return HttpResult.error("计算开始时间不能为空");
    	}
    	if(StringUtils.isBlank(map.get("etime"))) {
    		return HttpResult.error("计算结束时间不能为空");
    	}
        orgTotalService.countOrgTotal(map);
        return HttpResult.ok();
    }
    @PreAuthorize("hasAuthority('reward:orgtotal:del')")
    @RequestMapping("/delete")
    public HttpResult delete(@RequestBody Map<String,Object> map) throws Exception {
    	if(StringUtils.isBlank(map.get("btime"))) {
    		return HttpResult.error("计算开始时间不能为空");
    	}
    	if(StringUtils.isBlank(map.get("etime"))) {
    		return HttpResult.error("计算结束时间不能为空");
    	}
        orgTotalService.delOrgTotal(map);
        return HttpResult.ok();
    }
 	/**
    * 修改
    *
    * @param model
    * @return
 	 * @throws Exception 
    */
    @PreAuthorize("hasAuthority('reward:orgtotal:edit')")
    @RequestMapping("/handle")
    public HttpResult handle(@RequestBody List<OrgTotal> list) throws Exception {
        orgTotalService.handleTotal(list);
        return HttpResult.ok();
    }
    /**
    * 列表查询（分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('reward:orgtotal:view')")
    @RequestMapping("/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {
    	
    	pageRequest.getParams().put("etime@desc", "a");
        PageResult page = orgTotalService.findPage(pageRequest);
        // todo 再包装一层
        return HttpResult.ok(page);
    }

}

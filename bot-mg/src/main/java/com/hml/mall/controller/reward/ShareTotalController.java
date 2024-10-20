package com.hml.mall.controller.reward;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hml.core.http.HttpResult;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.reward.ShareTotal;
import com.hml.mall.iface.reward.IShareTotalService;
import com.hml.utils.StringUtils;


/**
*  前端控制器
* @author hml
* @since 2021-05-26
*/


@RestController
@RequestMapping("/shareTotal" )
public class ShareTotalController {

    @Autowired
    private IShareTotalService  shareTotalService;

    /**
    * 保存
    *
    * @param model
    * @return
     * @throws Exception 
    */
    @PreAuthorize("hasAuthority('reward:sharetotal:add')")
    @RequestMapping("/count")
    public HttpResult count(@RequestBody String fdate) throws Exception {
    	if(StringUtils.isBlank(fdate)) {
    		return HttpResult.error("计算日期不能为空");
    	}
    	shareTotalService.countShareTotal(fdate);
        return HttpResult.ok();
    }
 	/**
    * 修改
    *
    * @param model
    * @return
 	 * @throws Exception 
    */
    @PreAuthorize("hasAuthority('reward:sharetotal:edit')")
    @RequestMapping("/handle")
    public HttpResult handle(@RequestBody List<ShareTotal> list) throws Exception {
        shareTotalService.handleTotal(list);
        return HttpResult.ok();
    }
    /**
    * 列表查询（分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('reward:sharetotal:view')")
    @RequestMapping("/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {

        PageResult page = shareTotalService.findPage(pageRequest);
        // todo 再包装一层
        return HttpResult.ok(page);
    }

}

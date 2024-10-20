package com.hml.mall.controller.reward;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hml.core.http.HttpResult;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.reward.OrgHandle;
import com.hml.mall.iface.reward.IOrgHandleService;
import com.hml.mall.util.IPUtils;


/**
*  前端控制器
* @author hml
* @since 2021-05-26
*/


@RestController
@RequestMapping("/orgHandle" )
public class OrgHandleController {

    @Autowired
    private IOrgHandleService  orgHandleService;

  
 	/**
    * 修改
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('reward:orghandle:edit')")
    @RequestMapping("/audit")
    public HttpResult audit(@RequestBody List<OrgHandle> list,HttpServletRequest request) {
    	String ip = IPUtils.getIpAddr(request);
    	StringBuffer buff = new StringBuffer();
    	for(OrgHandle model : list) {
    		try {
    			model.setChkip(ip);
                orgHandleService.updateHandle(model);
			} catch (Exception e) {
				buff.append(model.getWaterno()).append(":").append(e.getMessage()).append("\r\n");
				e.printStackTrace();
			}
    	}
    	if(buff.toString().length() > 0) {
    		return HttpResult.error(buff.toString());
    	}
        return HttpResult.ok();
    }
	/**
    * 删除
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('reward:orghandle:del')")
    @RequestMapping("/delete")
    public HttpResult delete(@RequestBody OrgHandle model) {
        orgHandleService.removeById(model);
        return HttpResult.ok();
    }
    /**
    * 查询详情
    *
    * @param id
    * @return
    */
    @PreAuthorize("hasAuthority('reward:orghandle:view')")
    @RequestMapping("/getById")
    public HttpResult getById(Integer id) {
     OrgHandle model = orgHandleService.getById(id);
        // todo 再包装一层
        return HttpResult.ok(model);
    }


    /**
    * 列表查询（非分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('reward:orghandle:view')")
    @RequestMapping("/list")
    public HttpResult list() {
        OrgHandle model = new  OrgHandle();
        List<OrgHandle> list =  orgHandleService.list(model);
        // todo 再包装一层
        return HttpResult.ok(list);
    }


    /**
    * 列表查询（分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('reward:orghandle:view')")
    @RequestMapping("/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {

        PageResult page = orgHandleService.findPage(pageRequest);
        // todo 再包装一层
        return HttpResult.ok(page);
    }

}

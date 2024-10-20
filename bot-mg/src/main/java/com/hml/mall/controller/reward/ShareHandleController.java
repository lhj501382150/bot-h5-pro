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
import com.hml.mall.entity.reward.ShareHandle;
import com.hml.mall.iface.reward.IShareHandleService;
import com.hml.mall.util.IPUtils;


/**
*  前端控制器
* @author hml
* @since 2021-05-26
*/


@RestController
@RequestMapping("/shareHandle" )
public class ShareHandleController {

    @Autowired
    private IShareHandleService  shareHandleService;

 	/**
    * 修改
    *
    * @param model
    * @return
 	 * @throws Exception 
    */
    @PreAuthorize("hasAuthority('reward:sharehandle:edit')")
    @RequestMapping("/audit")
    public HttpResult audit(@RequestBody List<ShareHandle> list,HttpServletRequest request) throws Exception {
    	String ip = IPUtils.getIpAddr(request);
    	StringBuffer buff = new StringBuffer();
    	for(ShareHandle model : list) {
    		try {
    			model.setChkip(ip);
                shareHandleService.updateHandle(model);
			} catch (Exception e) {
				buff.append(model.getPdate()).append("-").append(model.getUserno()).append(":").append(e.getMessage()).append("\r\n");
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
    @PreAuthorize("hasAuthority('reward:sharehandle:del')")
    @RequestMapping("/delete")
    public HttpResult delete(@RequestBody ShareHandle model) {
        shareHandleService.removeById(model);
        return HttpResult.ok();
    }
    /**
    * 查询详情
    *
    * @param id
    * @return
    */
    @PreAuthorize("hasAuthority('reward:sharehandle:view')")
    @RequestMapping("/getById")
    public HttpResult getById(Integer id) {
     ShareHandle model = shareHandleService.getById(id);
        // todo 再包装一层
        return HttpResult.ok(model);
    }


    /**
    * 列表查询（非分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('reward:sharehandle:view')")
    @RequestMapping("/list")
    public HttpResult list() {
        ShareHandle model = new  ShareHandle();
        List<ShareHandle> list =  shareHandleService.list(model);
        // todo 再包装一层
        return HttpResult.ok(list);
    }


    /**
    * 列表查询（分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('reward:sharehandle:view')")
    @RequestMapping("/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {

        PageResult page = shareHandleService.findPage(pageRequest);
        // todo 再包装一层
        return HttpResult.ok(page);
    }

}

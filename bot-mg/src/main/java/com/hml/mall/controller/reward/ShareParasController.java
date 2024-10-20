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
import com.hml.mall.entity.reward.ShareParas;
import com.hml.mall.iface.reward.IShareParasService;


/**
*  前端控制器
* @author hml
* @since 2021-05-26
*/


@RestController
@RequestMapping("/shareParas" )
public class ShareParasController {
	
    @Autowired
    private IShareParasService  shareParasService;

    /**
    * 保存
    *
    * @param model
    * @return
     * @throws Exception 
    */
    @PreAuthorize("hasAuthority('reward:sharepara:add')")
    @RequestMapping("/save")
    public HttpResult save(@RequestBody ShareParas model) throws Exception {
//    	过滤重复
    	checkRepeat(model);
        shareParasService.save(model);
        return HttpResult.ok();
    }
 	/**
    * 修改
    *
    * @param model
    * @return
 	 * @throws Exception 
    */
    @PreAuthorize("hasAuthority('reward:sharepara:edit')")
    @RequestMapping("/edit")
    public HttpResult edit(@RequestBody ShareParas model) throws Exception {
//    	过滤重复
    	checkRepeat(model);
        shareParasService.updateById(model);
        return HttpResult.ok();
    }
	/**
    * 删除
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('reward:sharepara:del')")
    @RequestMapping("/delete")
    public HttpResult delete(@RequestBody ShareParas model) {
        shareParasService.removeById(model);
        return HttpResult.ok();
    }
    /**
    * 查询详情
    *
    * @param id
    * @return
    */
    @PreAuthorize("hasAuthority('reward:sharepara:view')")
    @RequestMapping("/getById")
    public HttpResult getById(Integer id) {
     ShareParas model = shareParasService.getById(id);
        // todo 再包装一层
        return HttpResult.ok(model);
    }


    /**
    * 列表查询（非分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('reward:sharepara:view')")
    @RequestMapping("/list")
    public HttpResult list() {
        ShareParas model = new  ShareParas();
        List<ShareParas> list =  shareParasService.list(model);
        // todo 再包装一层
        return HttpResult.ok(list);
    }


    /**
    * 列表查询（分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('reward:sharepara:view')")
    @RequestMapping("/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {

        PageResult page = shareParasService.findPage(pageRequest);
        // todo 再包装一层
        return HttpResult.ok(page);
    }
//	校验是否重复
    private void checkRepeat(ShareParas para)throws Exception{
    	List<ShareParas> list = shareParasService.list();
    	if(list.size() ==0) return;
    	for(ShareParas temp: list) {
    		if(temp.getId() == para.getId()) continue;
    		int min  = para.getMinnum();
    		int max = para.getMaxnum();
    		if((min <= temp.getMinnum() && max >= temp.getMinnum())
    			|| (min <= temp.getMaxnum() && max >= temp.getMaxnum())	
    			|| (min >= temp.getMinnum() && max <= temp.getMaxnum())
    			|| (min <= temp.getMinnum() && max >= temp.getMaxnum())
    		) {
    			throw new Exception("人数取值范围重叠，请重新设置");
    		}
    			
    	}
    }
}

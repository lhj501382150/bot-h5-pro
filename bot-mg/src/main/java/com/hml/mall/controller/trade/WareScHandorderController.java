package com.hml.mall.controller.trade;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hml.core.http.HttpResult;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.trade.WareScHandorder;
import com.hml.mall.entity.user.User;
import com.hml.mall.entity.user.UserDigitart;
import com.hml.mall.iface.trade.IWareScHandorderService;
import com.hml.mall.iface.user.IUserDigitartService;
import com.hml.mall.iface.user.IUserService;
import com.hml.utils.StringUtils;


/**
*  前端控制器
* @author hml
* @since 2022-05-13
*/


@RestController
@RequestMapping("/wareScHandorder" )
public class WareScHandorderController {

    @Autowired
    private IWareScHandorderService  wareScHandorderService;
    @Autowired
    private IUserDigitartService userDigitartService;
    @Autowired
    private IUserService userService;
    /**
    * 保存
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('trade:wareschandorder:add')")
    @RequestMapping("/save")
    public HttpResult save(@RequestBody WareScHandorder model) {
    	if(model.getArtid() != null){
    		UserDigitart item = checkArt(model);
    		if(item == null){
        		return HttpResult.error("请输入正确资产编号");
        	}
    		User user = userService.getById(item.getUserno());
    		StringBuilder sb = new StringBuilder();
    		sb.append("K1=").append(item.getCardname()).append("#");
    		sb.append("K2=").append(item.getCardgrade()).append("#");
    		sb.append("K3=").append(item.getBlocknum()).append("#");
    		sb.append("K4=").append(item.getTxhash()).append("#");
    		sb.append("K5=").append(item.getSpath()).append("#");
    		sb.append("K6=").append(user.getUsername()).append("#");
    		sb.append("K7=").append(item.getChkhash()).append("#");
    		sb.append("K8=").append(item.getMem()).append("#");
    		sb.append("K9=").append(item.getArttype());
    		model.setExt(sb.toString());
    		
    	}
    	
    	if(!checkUser(model)){
    		return HttpResult.error("请输入正确拍卖委托人");
    	}
    	model.setReqstatus("0");
        wareScHandorderService.save(model);
        return HttpResult.ok();
    }
 	/**
    * 修改
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('trade:wareschandorder:edit')")
    @RequestMapping("/edit")
    public HttpResult edit(@RequestBody WareScHandorder model) {
    	
    	if(!checkUser(model)){
    		return HttpResult.error("请输入正确拍卖委托人");
    	}
    	if(model.getArtid() != null){
    		UserDigitart item = checkArt(model);
    		if(item == null){
        		return HttpResult.error("请输入正确资产编号");
        	}
    		User user = userService.getById(item.getUserno());
    		StringBuilder sb = new StringBuilder();
    		sb.append("K1=").append(item.getCardname()).append("#");
    		sb.append("K2=").append(item.getCardgrade()).append("#");
    		sb.append("K3=").append(item.getBlocknum()).append("#");
    		sb.append("K4=").append(item.getTxhash()).append("#");
    		sb.append("K5=").append(item.getSpath()).append("#");
    		sb.append("K6=").append(user.getUsername()).append("#");
    		sb.append("K7=").append(item.getChkhash()).append("#");
    		sb.append("K8=").append(item.getMem()).append("#");
    		sb.append("K9=").append(item.getArttype());
    		model.setExt(sb.toString());
    		
    	}else{
    		model.setExt("");
    	}
    	WareScHandorder item = wareScHandorderService.getById(model.getHid());
    	if("1".equals(item.getReqstatus())){
    		return HttpResult.error("数据已审核，暂不能修改");
    	}
    	if("2".equals(item.getReqstatus())){
    		return HttpResult.error("数据已驳回，暂不能修改");
    	}
        wareScHandorderService.updateById(model);
        return HttpResult.ok();
    }
	/**
    * 删除
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('trade:wareschandorder:del')")
    @RequestMapping("/delete")
    public HttpResult delete(@RequestBody WareScHandorder model) {
    	WareScHandorder item = wareScHandorderService.getById(model.getHid());
    	if("1".equals(item.getReqstatus())){
    		return HttpResult.error("数据已审核，暂不能删除");
    	}
    	if("2".equals(item.getReqstatus())){
    		return HttpResult.error("数据已驳回，暂不能删除");
    	}
        wareScHandorderService.removeById(model);
        return HttpResult.ok();
    }
    @PreAuthorize("hasAuthority('trade:wareschandorder:audit')")
    @RequestMapping("/audit")
    public HttpResult audit(@RequestBody WareScHandorder model) throws Exception {
    	WareScHandorder item = wareScHandorderService.getById(model.getHid());
    	if(!"0".equals(item.getReqstatus())){
    		return HttpResult.error("数据已处理，请勿重复操作");
    	}
        wareScHandorderService.audit(model);
        return HttpResult.ok();
    }
    @PreAuthorize("hasAuthority('trade:wareschandorder:refuse')")
    @RequestMapping("/refuse")
    public HttpResult refuse(@RequestBody WareScHandorder model) throws Exception {
    	WareScHandorder item = wareScHandorderService.getById(model.getHid());
    	if(!"0".equals(item.getReqstatus())){
    		return HttpResult.error("数据已处理，请勿重复操作");
    	}
        wareScHandorderService.reject(model);
        return HttpResult.ok();
    }
    /**
    * 查询详情
    *
    * @param id
    * @return
    */
    @PreAuthorize("hasAuthority('trade:wareschandorder:view')")
    @RequestMapping("/getById")
    public HttpResult getById(Integer id) {
     WareScHandorder model = wareScHandorderService.getById(id);
        // todo 再包装一层
        return HttpResult.ok(model);
    }


    /**
    * 列表查询（非分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('trade:wareschandorder:view')")
    @RequestMapping("/list")
    public HttpResult list() {
        WareScHandorder model = new  WareScHandorder();
        List<WareScHandorder> list =  wareScHandorderService.list(model);
        // todo 再包装一层
        return HttpResult.ok(list);
    }


    /**
    * 列表查询（分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('trade:wareschandorder:view')")
    @RequestMapping("/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {

        PageResult page = wareScHandorderService.findPage(pageRequest);
        // todo 再包装一层
        return HttpResult.ok(page);
    }

//  true 存在 false 不存在
    private UserDigitart checkArt(WareScHandorder model){
    	 
    	UserDigitart item = userDigitartService.getById(model.getArtid());
    	return item;
    }
    
    private boolean checkUser(WareScHandorder model){
    	if(StringUtils.isBlank(model.getSuserno())){
    		return false;
    	}
    	User item = userService.getById(model.getSuserno());
    	return item != null;
    }
}

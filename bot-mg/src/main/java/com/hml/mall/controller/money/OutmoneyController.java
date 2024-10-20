package com.hml.mall.controller.money;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.hml.core.http.HttpResult;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.money.Outmoney;
import com.hml.mall.entity.user.UserCardinfo;
import com.hml.mall.iface.money.IOutmoneyService;
import com.hml.mall.iface.user.IUserCardinfoService;
import com.hml.mall.security.LoginUser;
import com.hml.mall.util.IPUtils;
import com.hml.mall.util.SecurityUtils;
import com.hml.utils.DateTimeUtils;


/**
*  前端控制器
* @author hml
* @since 2021-11-19
*/


@RestController
@RequestMapping("/outmoney" )
public class OutmoneyController {

    @Autowired
    private IOutmoneyService  outmoneyService;
    
    @Autowired
    private IUserCardinfoService userCardinfoService;

    /**
    * 保存
    *
    * @param model
    * @return
     * @throws Exception 
    */
    @PreAuthorize("hasAuthority('money:outmoney:add')")
    @RequestMapping("/save")
    public HttpResult save(@RequestBody Outmoney model) throws Exception {
    	LoginUser user = SecurityUtils.getLoginInfo();
    	if(user.getType() > 0) {
    		model.setUserno(user.getUserno());
    	}
    	model.setFdate(DateTimeUtils.getCurrentDate("yyyy-MM-dd"));
    	model.setPaychan("C");
    	model.setPaystatus("100");
    	UserCardinfo card = userCardinfoService.getById(model.getCardId());
    	if(card == null) {
    		throw new Exception("银行卡信息不正确！");
    	}
    	model.setReccustno(card.getCardno());
    	model.setCtime(DateTimeUtils.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
    	model.setChkstatus("0");
    	
    	model.setMemo("");
        outmoneyService.save(model);
        return HttpResult.ok();
    }
 	/**
    * 修改
    *
    * @param model
    * @return
 	 * @throws Exception 
    */
    @PreAuthorize("hasAuthority('money:outmoney:audit')")
    @RequestMapping("/audit")
    public HttpResult audit(@RequestBody Outmoney model,HttpServletRequest request) throws Exception {
    	model.setChkip(IPUtils.getIpAddr(request));
    	model.setChkuserno(SecurityUtils.getUsername());
    	model.setChkstatus("1");
        outmoneyService.checkOutMoney(model);
        return HttpResult.ok();
    }
	/**
    * 删除
    *
    * @param model
    * @return
	 * @throws Exception 
    */
    @PreAuthorize("hasAuthority('money:outmoney:reject')")
    @RequestMapping("/reject")
    public HttpResult reject(@RequestBody Outmoney model,HttpServletRequest request) throws Exception {
    	model.setChkip(IPUtils.getIpAddr(request));
    	model.setChkuserno(SecurityUtils.getUsername());
    	model.setChkstatus("2");
    	outmoneyService.checkOutMoney(model);
        return HttpResult.ok();
    }
    
    @PreAuthorize("hasAuthority('money:outmoney:pay')")
    @RequestMapping("/batchPay")
    public HttpResult batchPay(@RequestBody List<Outmoney> list)throws Exception{
    	List<Long> ids = new ArrayList<Long>();
    	for(Outmoney item:list) {
    		ids.add(item.getId());
    	}
    	outmoneyService.payOutMoney(ids);
    	return HttpResult.ok();
    }
    /**
    * 查询详情
    *
    * @param id
    * @return
    */
    @PreAuthorize("hasAuthority('money:outmoney:view')")
    @RequestMapping("/getById")
    public HttpResult getById(Integer id) {
     Outmoney model = outmoneyService.getById(id);
        // todo 再包装一层
        return HttpResult.ok(model);
    }


    /**
    * 列表查询（非分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('money:outmoney:view')")
    @RequestMapping("/list")
    public HttpResult list() {
        Outmoney model = new  Outmoney();
        List<Outmoney> list =  outmoneyService.list(model);
        // todo 再包装一层
        return HttpResult.ok(list);
    }


    /**
    * 列表查询（分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('money:outmoney:view')")
    @RequestMapping("/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {

        PageResult page = outmoneyService.findPage(pageRequest);
        // todo 再包装一层
        return HttpResult.ok(page);
    }

    /**
     * 列表查询（分页）
     *
     * @return
     */
     @PreAuthorize("hasAuthority('money:outmoney:view')")
     @RequestMapping("/findExport")
     public HttpResult findExportPage(@RequestBody PageRequest pageRequest) {

         PageResult page = outmoneyService.findExportPage(pageRequest);
         // todo 再包装一层
         return HttpResult.ok(page);
     }
    
}

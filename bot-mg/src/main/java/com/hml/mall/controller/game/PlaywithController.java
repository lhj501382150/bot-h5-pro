package com.hml.mall.controller.game;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hml.core.http.HttpResult;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.game.Playwith;
import com.hml.mall.entity.user.UserRelation;
import com.hml.mall.iface.game.IPlaywithService;
import com.hml.mall.iface.user.IUserRelationService;
import com.hml.mall.security.LoginUser;
import com.hml.mall.util.IPUtils;
import com.hml.mall.util.SecurityUtils;
import com.hml.utils.DateTimeUtils;
import com.hml.utils.StringUtils;


/**
*  前端控制器
* @author hml
* @since 2021-06-28
*/


@RestController
@RequestMapping("/playwith" )
public class PlaywithController {

    @Autowired
    private IPlaywithService  playwithService;

    @Autowired
    private IUserRelationService userRelationService;
    /**
    * 保存
    *
    * @param model
    * @return
     * @throws Exception 
    */
    @PreAuthorize("hasAuthority('gm:playwith:add')")
    @RequestMapping("/save")
    public HttpResult save(@RequestBody Playwith model,HttpServletRequest request) throws Exception {
    	int count = checkUser(model.getUserno());
    	if(count <= 0) {
    		return HttpResult.error("账号不存在");
    	}
    	Playwith id = playwithService.getById(model.getUserno());
    	if(id != null) {
    		return HttpResult.error("数据已存在！");
    	}
    	fillData(model, request);
    	model.setCheckstatus("0");
        playwithService.save(model);
        return HttpResult.ok();
    }
 	/**
    * 修改
    *
    * @param model
    * @return
 	 * @throws Exception 
    */
    @PreAuthorize("hasAuthority('gm:playwith:edit')")
    @RequestMapping("/edit")
    public HttpResult edit(@RequestBody Playwith model,HttpServletRequest request) throws Exception {
    	int count = checkUser(model.getUserno());
    	if(count <= 0) {
    		return HttpResult.error("账号不存在");
    	}
    	QueryWrapper<Playwith> qw = new QueryWrapper<>();
    	qw.eq("ACTNO", model.getActno());
    	qw.eq("USERNO", model.getUserno());
    	qw.eq("PTYPE", model.getPtype());
    	Playwith playwith = playwithService.getOne(qw);
    	if(!"3".equals(playwith.getCheckstatus())){
    		return HttpResult.error("只有已申请数据，才可执行此操作！");
    	}
    	fillData(model, request);
        playwithService.updateById(model);
        return HttpResult.ok();
    }
	/**
    * 删除
    *
    * @param model
    * @return
	 * @throws Exception 
    */
    @PreAuthorize("hasAuthority('gm:playwith:del')")
    @RequestMapping("/delete")
    public HttpResult delete(@RequestBody Playwith model) throws Exception {
    	if(model == null){
    		return HttpResult.error("传入参数不正确！");
    	} 
    	if(StringUtils.isBlank(model.getActno()) 
    			|| StringUtils.isBlank(model.getPtype())
    			|| StringUtils.isBlank(model.getUserno())){
    		return HttpResult.error("参数不能为空");
    	}
    	QueryWrapper<Playwith> qw = new QueryWrapper<>();
    	qw.eq("ACTNO", model.getActno());
    	qw.eq("USERNO", model.getUserno());
    	qw.eq("PTYPE", model.getPtype());
        playwithService.remove(qw);
        return HttpResult.ok();
    }
    /**
    * 查询详情
    *
    * @param id
    * @return
    */
    @PreAuthorize("hasAuthority('gm:playwith:view')")
    @RequestMapping("/getById")
    public HttpResult getById(Integer id) {
     Playwith model = playwithService.getById(id);
        // todo 再包装一层
        return HttpResult.ok(model);
    }


    /**
    * 列表查询（非分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('gm:playwith:view')")
    @RequestMapping("/list")
    public HttpResult list() {
        Playwith model = new  Playwith();
        List<Playwith> list =  playwithService.list(model);
        // todo 再包装一层
        return HttpResult.ok(list);
    }


    /**
    * 列表查询（分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('gm:playwith:view')")
    @RequestMapping("/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {

        PageResult page = playwithService.findPage(pageRequest);
        // todo 再包装一层
        return HttpResult.ok(page);
    }

    private void fillData(Playwith model,HttpServletRequest request) {
    	model.setOpaddr(IPUtils.getIpAddr(request));
    	model.setOptime(DateTimeUtils.getDateTime());
    	if(StringUtils.isBlank(model.getOpuser())){
    		model.setOpuser(SecurityUtils.getUsername());
    	}
    }

    private int checkUser(String userno)throws Exception{
    	LoginUser user = SecurityUtils.getLoginInfo();
    	QueryWrapper<UserRelation> qw = new QueryWrapper<>();
    	qw.eq("userno", userno);
    	if(user.getType() > 0){
    		String userKey = user.getQueryNo();
    		qw.eq(userKey, user.getUserno());
    	}
    	int count = userRelationService.count(qw);
    	return count;
    }
}

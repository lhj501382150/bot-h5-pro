package com.hml.mall.controller.game;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.hml.core.http.HttpResult;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.game.Team;
import com.hml.mall.entity.user.UserRelation;
import com.hml.mall.iface.game.ITeamService;
import com.hml.mall.iface.user.IUserRelationService;
import com.hml.mall.security.LoginUser;
import com.hml.mall.util.SecurityUtils;


/**
*  前端控制器
* @author hml
* @since 2021-06-28
*/


@RestController
@RequestMapping("/team" )
public class TeamController {

    @Autowired
    private ITeamService  teamService;
    @Autowired
    private IUserRelationService userRelationService;
    /**
    * 保存
    *
    * @param model
    * @return
     * @throws Exception 
    */
    @PreAuthorize("hasAuthority('gm:team:add')")
    @RequestMapping("/save")
    public HttpResult save(@RequestBody Team model) throws Exception {
    	QueryWrapper<Team> qw = new QueryWrapper<Team>();
    	qw.eq("userno", model.getUserno());
    	qw.eq("actno",model.getActno());
    	int count = teamService.count(qw);
    	if(count > 0){
    		return HttpResult.error("该客户已报名该活动");
    	}
        teamService.save(model);
        return HttpResult.ok();
    }
 	/**
    * 修改
    *
    * @param model
    * @return
 	 * @throws Exception 
    */
    @PreAuthorize("hasAuthority('gm:team:edit')")
    @RequestMapping("/edit")
    public HttpResult edit(@RequestBody Team model) throws Exception {
    	int count = checkUser(model.getUserno());
    	if(count <= 0) {
    		return HttpResult.error("客户不存在");
    	}
    	UpdateWrapper<Team> uw = new UpdateWrapper<Team>();
    	uw.eq("userno", model.getUserno());
    	uw.eq("actno",model.getActno());
        teamService.update(uw);
        return HttpResult.ok();
    }
	/**
    * 删除
    *
    * @param model
    * @return
	 * @throws Exception 
    */
    @PreAuthorize("hasAuthority('gm:team:del')")
    @RequestMapping("/delete")
    public HttpResult delete(@RequestBody Team model) throws Exception {
    	 
    	QueryWrapper<Team> qw = new QueryWrapper<Team>();
    	qw.eq("userno", model.getUserno());
    	qw.eq("actno",model.getActno());
        teamService.remove(qw);
        return HttpResult.ok();
    }
    /**
    * 查询详情
    *
    * @param id
    * @return
    */
    @PreAuthorize("hasAuthority('gm:team:view')")
    @RequestMapping("/getById")
    public HttpResult getById(Integer id) {
     Team model = teamService.getById(id);
        // todo 再包装一层
        return HttpResult.ok(model);
    }


    /**
    * 列表查询（非分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('gm:team:view')")
    @RequestMapping("/list")
    public HttpResult list() {
        Team model = new  Team();
        List<Team> list =  teamService.list(model);
        // todo 再包装一层
        return HttpResult.ok(list);
    }


    /**
    * 列表查询（分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('gm:team:view')")
    @RequestMapping("/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {

        PageResult page = teamService.findPage(pageRequest);
        // todo 再包装一层
        return HttpResult.ok(page);
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

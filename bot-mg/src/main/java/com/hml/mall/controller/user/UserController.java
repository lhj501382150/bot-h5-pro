package com.hml.mall.controller.user;


import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

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
import com.hml.mall.entity.money.UsermoneyChangeApply;
import com.hml.mall.entity.sys.Login;
import com.hml.mall.entity.user.User;
import com.hml.mall.entity.user.UserRelation;
import com.hml.mall.iface.money.IUsermoneyChangeApplyService;
import com.hml.mall.iface.sys.ILoginService;
import com.hml.mall.iface.user.IUserRelationService;
import com.hml.mall.iface.user.IUserService;
import com.hml.mall.security.LoginUser;
import com.hml.mall.util.IPUtils;
import com.hml.mall.util.PasswordEncoder;
import com.hml.mall.util.SecurityUtils;
import com.hml.redis.RedisKey;
import com.hml.redis.RedisUtils;
import com.hml.utils.DateTimeUtils;
import com.hml.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;


/**
*  前端控制器
* @author hml
* @since 2021-04-11
*/

@Slf4j
@RestController
@RequestMapping("/user" )
public class UserController {

    @Autowired
    private IUserService  userService;
    
    @Autowired
    private IUserRelationService userRelationService;
    
    @Autowired
    private IUsermoneyChangeApplyService userMoneyChangeApplyService;
    
    @Autowired
    private ILoginService loginService;
    
    @Autowired
    private RedisUtils redisUtils;
    
    
    @PreAuthorize("hasAuthority('firm:member:addMoney')")
    @RequestMapping("/addMoney")
    public HttpResult addMoney(@RequestBody UsermoneyChangeApply model,HttpServletRequest request) {
        try {
        	if(StringUtils.isBlank(model.getUserno())) {
        		return HttpResult.error("用户不能为空！");
        	}
        	if(StringUtils.isBlank(model.getChmoney())) {
        		return HttpResult.error("变动金额不能为空！");
        	}
        	LoginUser user = SecurityUtils.getLoginInfo();
        	if(user == null) {
        		throw new Exception("登录已失效，请重新登录");
        	}
        	if(user.getType() > 0) {
        		QueryWrapper<UserRelation> qw = new QueryWrapper<UserRelation>();
	   	   		 qw.eq("parentno", user.getUserno());
	   	   		 qw.eq("userno", model.getUserno());
	   	   		 Integer count = userRelationService.count(qw);
	   	   		 if(count <= 0) {
	   	   			 return HttpResult.error("暂无权限执行此操作");
	   	   		 }
	   	   		 
	   	   		 BigDecimal leftmoney = userService.checkMoney();
	   	   		 BigDecimal money = leftmoney.subtract(model.getChmoney());
	   	   		 if(money.doubleValue() < 0) {
	   	   			 return HttpResult.error("可支配额度不足,当前剩余额度:" + leftmoney);
	   	   		 }
	   	   		 
        	}
        	
        	model.setAcctno("100");
        	model.setSubno("100");
        	model.setChkip(IPUtils.getIpAddr(request));
        	model.setChktime(DateTimeUtils.getDateTime());
        	model.setChkuser(SecurityUtils.getUsername());
        	model.setStatus(0);
        	userMoneyChangeApplyService.saveAndAudit(model);
        	log.info("客户上分：【{}】-{}：{}",user.getLoginno(),model.getUserno(),model.getChmoney());
			return HttpResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return HttpResult.error(e.getMessage());
		}
        
    }
    
    @PreAuthorize("hasAuthority('firm:member:subMoney')")
    @RequestMapping("/subMoney")
    public HttpResult subMoney(@RequestBody UsermoneyChangeApply model,HttpServletRequest request) {
        try {
        	if(StringUtils.isBlank(model.getUserno())) {
        		return HttpResult.error("用户不能为空！");
        	}
        	if(StringUtils.isBlank(model.getChmoney())) {
        		return HttpResult.error("变动金额不能为空！");
        	}
        	LoginUser user = SecurityUtils.getLoginInfo();
        	if(user.getType() > 0) {
        		QueryWrapper<UserRelation> qw = new QueryWrapper<UserRelation>();
	   	   		 qw.eq("parentno", user.getUserno());
	   	   		 qw.eq("userno", model.getUserno());
	   	   		 Integer count = userRelationService.count(qw);
	   	   		 if(count <= 0) {
	   	   			 return HttpResult.error("暂无权限执行此操作");
	   	   		 }
        	}
        	model.setAcctno("100");
        	model.setSubno("101");
        	model.setChkip(IPUtils.getIpAddr(request));
        	model.setChktime(DateTimeUtils.getDateTime());
        	model.setChkuser(SecurityUtils.getUsername());
        	model.setStatus(0);
        	userMoneyChangeApplyService.saveAndAudit(model);
        	log.info("客户下分：【{}】-{}：{}",user.getLoginno(),model.getUserno(),model.getChmoney());
			return HttpResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return HttpResult.error(e.getMessage());
		}
        
    }
    
    @PreAuthorize("hasAuthority('firm:member:clear')")
    @RequestMapping("/clean")
    public HttpResult clean() {
        try {
        	LoginUser user = SecurityUtils.getLoginInfo();
        	if(user.getType() > 0) {
        		return HttpResult.error("暂无权限执行此操作");
        	}
        	userService.clearMoney();
        	log.info("清除积分：【{}】",user.getUserno());
			return HttpResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return HttpResult.error(e.getMessage());
		}
        
    }
    /**
    * 保存
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('firm:member:add')")
    @RequestMapping("/save")
    public HttpResult save(@RequestBody User model) {
        try {
        	String regDate = DateTimeUtils.getCurrentDate("");
        	model.setRegdate(regDate);
			userService.saveUser(model);
			return HttpResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return HttpResult.error(e.getMessage());
		}
        
    }
 	/**
    * 修改
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('firm:member:edit')")
    @RequestMapping("/edit")
    public HttpResult edit(@RequestBody User model) {
        try {
        	User item = userService.getById(model.getUserno());
        	if(item == null) {
        		return HttpResult.error("用户不能为空");
        	}
        	model.setPaypwd(item.getPaypwd());
			userService.updateUser(model);
			return HttpResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return HttpResult.error(e.getMessage());
		}
    }
    @PreAuthorize("hasAuthority('firm:member:edit')")
    @RequestMapping("/editPwd")
    public HttpResult editPwd(@RequestBody User model,HttpServletRequest request) {
        try {
        	LoginUser user = SecurityUtils.getLoginInfo();
        	if(user.getType() > 0) {
        		QueryWrapper<UserRelation> qw = new QueryWrapper<UserRelation>();
	   	   		 qw.eq("uno" + user.getClevel(), user.getUserno());
	   	   		 qw.eq("userno", model.getUserno());
	   	   		 Integer count = userRelationService.count(qw);
	   	   		 if(count <= 0) {
	   	   			 throw new Exception("暂无权限执行此操作");
	   	   		 }
        	}
        	User item = userService.getById(model.getUserno());
        	if(item == null) {
        		return HttpResult.error("用户不能为空");
        	}
        	String pwd = model.getUserno() + model.getPaypwd();
        	pwd = new PasswordEncoder("").encode(pwd);
        	item.setPaypwd(pwd);
			userService.updateById(item);
			
			Login login = loginService.getById(item.getUserno());
			if(login != null) {
				login.setLoginpwd(pwd);
				loginService.updateById(login);	
			}	
			redisUtils.del(RedisKey.USER_PWD + item.getUserno());
			log.info("客户密码修改：【{}】-{}：{}",user.getLoginno(),model.getUserno(),IPUtils.getIpAddr(request));
			return HttpResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return HttpResult.error(e.getMessage());
		}
    }
    /**
     * 修改
     *
     * @param model
     * @return
     */
     @PreAuthorize("hasAuthority('firm:member:edit')")
     @RequestMapping("/editStatus")
     public HttpResult editStatus(@RequestBody User model) {
         try {
        	 LoginUser user = SecurityUtils.getLoginInfo();
         	if(user.getType() > 0) {
         		QueryWrapper<UserRelation> qw = new QueryWrapper<UserRelation>();
 	   	   		 qw.eq("uno" + user.getClevel(), user.getUserno());
 	   	   		 qw.eq("userno", model.getUserno());
 	   	   		 Integer count = userRelationService.count(qw);
 	   	   		 if(count <= 0) {
 	   	   			 return HttpResult.error("暂无权限执行此操作");
 	   	   		 }
         	}
 			userService.updateStatus(model);
 			log.info("客户状态修改：【{}】-{}：{}",user.getLoginno(),model.getUserno(),model.getSex());
 			return HttpResult.ok();
 		} catch (Exception e) {
 			e.printStackTrace();
 			return HttpResult.error(e.getMessage());
 		}
     }
    /**
     * 修改
     *
     * @param model
     * @return
     */
    @PreAuthorize("hasAuthority('firm:member:del')")
    @RequestMapping("/delete")
    public HttpResult delete(@RequestBody User model) {
    	 try {
 			userService.deleteUser(model);
 			return HttpResult.ok();
 		} catch (Exception e) {
 			e.printStackTrace();
 			return HttpResult.error(e.getMessage());
 		}
    }

    /**
    * 查询详情
    *
    * @param id
    * @return
     * @throws Exception 
    */
    @RequestMapping("/getById")
    public HttpResult getById(@RequestBody User model) throws Exception {
    	LoginUser user = SecurityUtils.getLoginInfo();
    	if(user.getType() > 0) {
    		QueryWrapper<UserRelation> qw = new QueryWrapper<UserRelation>();
   	   		 qw.eq("uno" + user.getClevel(), user.getUserno());
   	   		 qw.eq("userno", model.getUserno());
   	   		 Integer count = userRelationService.count(qw);
   	   		 if(count <= 0) {
   	   			 throw new Exception("暂无权限执行此操作");
   	   		 }
    	}
      model = userService.getById(model.getUserno());
        // todo 再包装一层
        return HttpResult.ok(model);
    }


    /**
    * 列表查询（非分页）
    *
    * @return
    */
    @RequestMapping("/list")
    public HttpResult list(@RequestBody User model) {
        List<User> list =  userService.list(model);
        // todo 再包装一层
        return HttpResult.ok(list);
    }
    /**
     * 列表查询（非分页）
     *
     * @return
     */
     @PreAuthorize("hasAuthority('firm:member:view')")
     @RequestMapping("/findParent")
     public HttpResult findParent(@RequestBody User model) {
    	 model.setOrgtype(1);
    	 model.setIsvalid("Y");
    	 
         List<User> list =  userService.list(model);
         // todo 再包装一层
         return HttpResult.ok(list);
     }
 
    /**
    * 列表查询（分页）
    *
    * @return
    */
//    @PreAuthorize("hasAuthority('firm:member:view')")
    @RequestMapping("/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {
        PageResult page = userService.findPage(pageRequest);
        return HttpResult.ok(page);
    }
    
    @RequestMapping("/findOrgNum")
    public HttpResult findOrgNum() throws Exception {
        List<Map<String, Object>> page = userService.findOrgCount();
        return HttpResult.ok(page);
    }

}

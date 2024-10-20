package com.hml.mall.controller.sys;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.hml.core.http.HttpResult;
import com.hml.mall.entity.sys.Login;
import com.hml.mall.entity.sys.LoginLog;
import com.hml.mall.iface.sys.ILoginLogService;
import com.hml.mall.iface.sys.ILoginService;
import com.hml.mall.security.JwtAuthenticatioToken;
import com.hml.mall.util.IPUtils;
import com.hml.mall.util.PasswordUtils;
import com.hml.mall.util.SecurityUtils;
import com.hml.mall.vo.LoginBean;
import com.hml.utils.DateTimeUtils;
import com.hml.utils.IOUtils;
import com.hml.utils.StringUtils;

/**
 * 登录控制器
 * @author hml
 * @date Jan 14, 2019
 */
@RestController
public class LoginController {

	@Autowired
	private Producer producer;
	@Autowired
	private ILoginService loginService;
	
	@Autowired
	private ILoginLogService loginLogService;
	
	@Autowired
	private AuthenticationManager authenticationManager;

	@GetMapping("captcha.jpg")
	public void captcha(HttpServletResponse response, HttpServletRequest request) throws ServletException, IOException {
		response.setHeader("Cache-Control", "no-store, no-cache");
		response.setContentType("image/jpeg");

		// 生成文字验证码
		String text = producer.createText();
		// 生成图片验证码
		BufferedImage image = producer.createImage(text);
		// 保存到验证码到 session
		request.getSession().setAttribute(Constants.KAPTCHA_SESSION_KEY, text);

		ServletOutputStream out = response.getOutputStream();
		ImageIO.write(image, "jpg", out);	
		IOUtils.closeQuietly(out);
	}

	/**
	 * 登录接口
	 */
	@PostMapping(value = "/login")
	public HttpResult login(@RequestBody LoginBean loginBean, HttpServletRequest request) throws IOException {
		String username = loginBean.getAccount();
		String password = loginBean.getPassword();
		String captcha = loginBean.getCaptcha();
		// 从session中获取之前保存的验证码跟前台传来的验证码进行匹配
		Object kaptcha = request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
		String msg = "";
		boolean flag = true;
		if(kaptcha == null){
			msg = "验证码已失效";
			flag = false;
		}
		if(flag && !captcha.equals(kaptcha)){
			msg = "验证码不正确";
			flag = false;
		}
		// 用户信息
		Login user = loginService.findByName(username);
		// 账号不存在、密码错误
		if(flag){
			if (user == null) {
				msg = "账号不存在";
				
			}else{
				if (!PasswordUtils.matches(user.getUserno(), password, user.getLoginpwd())) {
					msg = "密码不正确";
				}
				
				// 账号锁定
				if ("N".equals(user.getIsvalid())) {
					msg = "账号已被禁用,请联系管理员";
				}
			}
		} 
		
		
		LoginLog log = new LoginLog();
		log.setLoginno(loginBean.getAccount());
		if(user != null) {
			log.setUserno(user.getUserno());
		}
		String time = DateTimeUtils.getDateTime();
		String ip = IPUtils.getIpAddr(request);
		log.setDetype("PC");
		log.setToken("");
		log.setLogtime(time);
		log.setIpaddr(ip);
		log.setInorout(1);
		
		if(!StringUtils.isBlank(msg)) {
			log.setMemo(msg);
			log.setIssuccess(2);
			loginLogService.save(log);
			return HttpResult.error(msg);
		}else {
			// 系统登录认证
			JwtAuthenticatioToken token = SecurityUtils.login(request, username, password, authenticationManager);
			log.setMemo("登陆成功");
			log.setIssuccess(1);
			loginLogService.save(log);
//			更新登陆用户信息
			user.setLogtime(time);
			user.setToken("");
			user.setIpaddr(ip);
			user.setInorout(1);
			loginService.updateById(user);
			Map<String,Object> data = new HashMap<>();
			data.put("token", token.getToken());
			data.put("type", user.getType());
			return HttpResult.ok(data);
		}
	}

	@RequestMapping("/logOut")
	public String logout(HttpServletRequest request) {
		Login user = loginService.findByName(SecurityUtils.getUsername());
		LoginLog log = new LoginLog();
		String time = DateTimeUtils.getDateTime();
		String ip = IPUtils.getIpAddr(request);
		log.setLoginno(SecurityUtils.getUsername());
		if(user != null) {
			log.setUserno(user.getUserno());
		}
		log.setDetype("PC");
		log.setToken("");
		log.setLogtime(time);
		log.setIpaddr(ip);
		log.setInorout(2);
		log.setMemo("成功退出");
		log.setIssuccess(1);
		loginLogService.save(log);
		
//			更新登陆用户信息
		user.setLogtime(time);
		user.setToken("");
		user.setInorout(1);
		user.setIpaddr(ip);
		loginService.updateById(user);
		return "forward:/logout";
	}
}

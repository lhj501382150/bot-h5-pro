package com.hml.mall.security;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import com.hml.mall.entity.sys.Login;
import com.hml.mall.entity.sys.LoginLog;
import com.hml.mall.iface.sys.ILoginLogService;
import com.hml.mall.iface.sys.ILoginService;
import com.hml.mall.util.IPUtils;
import com.hml.mall.util.SecurityUtils;
import com.hml.utils.DateTimeUtils;
/**
 * 成功退出处理
 * @author Administrator
 *
 */
public class WriteLogoutSuccessHandler extends HttpStatusReturningLogoutSuccessHandler {

	private ILoginLogService loginLogService;
	
	private ILoginService loginService;
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException {
		try {
			Login user = loginService.findByName(SecurityUtils.getUsername());
			LoginLog log = new LoginLog();
			log.setLoginno(SecurityUtils.getUsername());
			if(user != null) {
				log.setUserno(user.getUserno());
			}
			log.setDetype("PC");
			log.setToken("");
			log.setLogtime(DateTimeUtils.getDateTime());
			log.setIpaddr(IPUtils.getIpAddr(request));
			log.setInorout(2);
			log.setMemo("成功退出");
			log.setIssuccess(1);
			loginLogService.save(log);
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onLogoutSuccess(request, response, authentication);
	}
	public WriteLogoutSuccessHandler(ILoginLogService loginLogService, ILoginService loginService) {
		this.loginLogService = loginLogService;
		this.loginService = loginService;
	}
}

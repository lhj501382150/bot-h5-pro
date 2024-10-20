package com.hml.mall.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.hml.mall.util.IPUtils;
import com.hml.mall.util.SecurityUtils;

/**
 * 登录认证过滤器
 * @author hml
 * @date Jan 14, 2019
 */
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

	
	@Autowired
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
    	// 获取token, 并检查登录状态
//    	System.out.println("请求地址: " + IPUtils.getIpAddr(request) + ":" + request.getRemotePort());
//    	System.out.println("请求路径getRequestURI:" + request.getRequestURI());
        SecurityUtils.checkAuthentication(request);
        chain.doFilter(request, response);
    }
    
}
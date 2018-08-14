package com.chenxing.managesystem.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

import com.chenxing.managesystem.interceptor.MyFilterSecurityInterceptor;


/**
 * 启动初始化访问路径
 * 
 * @author huayu
 *
 */
@Configuration
@EnableWebSecurity
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private MyFilterSecurityInterceptor myFilterSecurityInterceptor;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/css/**").permitAll().anyRequest().authenticated() // 任何请求,登录后可以访问
				.and().formLogin().loginPage("/login").defaultSuccessUrl("/").failureUrl("/login?error").permitAll() // 登录页面用户任意访问
				.and().logout().permitAll(); // 注销行为任意访问
		http.addFilterBefore(myFilterSecurityInterceptor, FilterSecurityInterceptor.class);
	}
}
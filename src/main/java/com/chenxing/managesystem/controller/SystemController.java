package com.chenxing.managesystem.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统功能类
 * <p>
 * Created by liuxing on 17/1/18.
 */
@RestController
public class SystemController {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	// @RequestMapping("/system/user/list")
	// public String goHomePage(Model model) {
	// UserDetails userDetails = (UserDetails)
	// SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	// SysUser user = new SysUser();
	// user.setUsername(userDetails.getUsername());
	// model.addAttribute("user", user);
	// return "homepage";
	// }
	//
	// @RequestMapping(value = "/system/user/list", method = RequestMethod.GET)
	// public BaseResult<Object> getUserList(Model model, @RequestParam String id,
	// @RequestParam String name,
	// @RequestParam int currentpage,
	// @RequestParam int pagesize) {
	// BaseResult<Object> result = new BaseResult<>();
	// log.info(name);
	// long start = System.currentTimeMillis();
	//
	//
	// long end = System.currentTimeMillis();
	// log.info("消耗时长 " + (start - end) + "毫秒");
	// result.setData("");
	// return result;
	//
	// }

}

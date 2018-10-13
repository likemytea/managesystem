package com.chenxing.managesystem.controller;

import java.text.ParseException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.chenxing.common.util.DateUtil;
import com.chenxing.common.vo.PageResult;
import com.chenxing.managesystem.domain.Leave;
import com.chenxing.managesystem.service.OaLeaveWorkFlowService;
import com.chenxing.managesystem.util.Variable;

/**
 * 流程引擎类：请假
 * <p>
 * Created by liuxing on 17/1/18.
 */
@RestController
@RequestMapping(value = "/workflow/leave")
public class FlowLeaveController {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	OaLeaveWorkFlowService oaLeaveWorkFlowService;

	@Value("${date.format.standard}")
	String dateFmt;

	@ResponseBody
	@RequestMapping(value = "/start", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public String start(@RequestParam String applyTime, @RequestParam String endTime,
			@RequestParam String leaveType, @RequestParam String realityEndTime, @RequestParam String realityStartTime,
			@RequestParam String reason, @RequestParam String startTime) throws ParseException {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		log.info("security holder-username：" + userDetails.getUsername());

		Leave l = new Leave();
		l.setApplyTime(DateUtil.str2Date(applyTime, dateFmt));
		l.setEndTime(endTime);
		l.setLeaveType(leaveType);
		l.setRealityEndTime(DateUtil.str2Date(realityEndTime, dateFmt));
		l.setRealityStartTime(DateUtil.str2Date(realityStartTime, dateFmt));
		l.setReason(reason);
		l.setStartTime(startTime);
		l.setUserId(userDetails.getUsername());
		oaLeaveWorkFlowService.startProcess(l);
		return "hello,leave!";
	}

	/**
	 * 获取待办任务列表
	 * 
	 */
	@ResponseBody
	@RequestMapping(value = "/findTasks", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public String findTasks(@RequestParam int currentpage, @RequestParam int pagesize) throws ParseException {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		log.info("security holder-username：" + userDetails.getUsername());
		PageResult<Leave> res = oaLeaveWorkFlowService.getTasks(userDetails.getUsername(), currentpage, pagesize);
		log.info("user-数组" + JSON.toJSONString(res));
		return JSON.toJSONString(res);
	}

	/**
	 * 执行任务
	 *
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/complete", method = { RequestMethod.POST, RequestMethod.GET })
	public String execTask(@RequestParam String taskid, Variable var) {
		if (StringUtils.isEmpty(taskid)) {
			return "paramError";
		}
		Map<String, Object> variables = var.getVariableMap();
		oaLeaveWorkFlowService.complete(taskid, variables);
		return "finished";
	}
}

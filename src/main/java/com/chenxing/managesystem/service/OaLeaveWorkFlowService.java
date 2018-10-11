package com.chenxing.managesystem.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chenxing.common.distributedKey.PrimarykeyGenerated;
import com.chenxing.managesystem.dao.OaLeaveDao;
import com.chenxing.managesystem.domain.Leave;

/**
 * 死刑复审
 * 
 * * Created by liuxing.
 */
@Service
@Transactional
public class OaLeaveWorkFlowService {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	// 注入为我们自动配置好的服务
	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private IdentityService identityService;

	@Autowired
	private OaLeaveDao oaLeaveDao;
	@Value("${process.key.oaleave}")
	String keyOaLeave;

	// 开始流程，传入申请者的id以及公司的id
	public void startProcess(Leave leave) {
		ProcessInstance processInstance = null;
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("applyTime", leave.getApplyTime());
		variables.put("reason", leave.getReason());
		// TODO;activiti的各个事务如何在此service层保持
		try {
			// 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
			identityService.setAuthenticatedUserId(leave.getUserId());

			// 启动流程
			processInstance = runtimeService.startProcessInstanceByKey(keyOaLeave,
					PrimarykeyGenerated.generateId(false), variables);
			leave.setProcessInstanceId(processInstance.getId());
			int count = oaLeaveDao.insertOaLeave(leave);
			if (count <= 0) {
				throw new Exception("insert oa_leave failed ");
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} finally {
			identityService.setAuthenticatedUserId(null);
		}

	}

	// 获得某个人的任务别表,参数是受托人
	public List<Task> getTasks(String assignee) {
		return taskService.createTaskQuery().taskCandidateUser(assignee).list();
	}

	// 完成任务
	public void completeTasks(Boolean joinApproved, String taskId) {
		Map<String, Object> taskVariables = new HashMap<String, Object>();
		taskVariables.put("joinApproved", joinApproved);
		taskService.complete(taskId, taskVariables);
	}
}

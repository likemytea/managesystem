package com.chenxing.managesystem.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chenxing.common.distributedKey.PrimarykeyGenerated;
import com.chenxing.common.vo.PageResult;
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
	protected RepositoryService repositoryService;

	@Autowired
	private OaLeaveDao oaLeaveDao;
	@Value("${process.key.oaleave}")
	String keyOaLeave;

	// 开始流程，传入申请者的id以及公司的id
	public void startProcess(Leave leave) {
		leave.setId(Long.parseLong(PrimarykeyGenerated.generateId(false)));
		ProcessInstance processInstance = null;
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("applyTime", leave.getApplyTime());
		variables.put("reason", leave.getReason());
		// TODO;liuxingactiviti的各个事务如何在此service层保持
		try {
			// 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
			identityService.setAuthenticatedUserId(leave.getUserId());

			// 启动流程
			processInstance = runtimeService.startProcessInstanceByKey(keyOaLeave,
					String.valueOf(leave.getId()), variables);
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
	public List<Leave> getTasks(String assignee, int currentpage, int pagesize) {
		TaskQuery taskQuery = taskService.createTaskQuery().taskCandidateOrAssigned(assignee);
		List<Task> tasks = taskQuery.list();
		StringBuffer leavePK = new StringBuffer();
		List<Leave> results = new ArrayList<Leave>();
		// 根据流程的业务ID查询实体并关联
		for (Task task : tasks) {
			String processInstanceId = task.getProcessInstanceId();
			ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
					.processInstanceId(processInstanceId).active().singleResult();
			String businessKey = processInstance.getBusinessKey();

			if (businessKey == null) {
				continue;
			}
			leavePK.append(businessKey);
			leavePK.append(",");

			Leave leave = new Leave();
			leave.setId(Long.parseLong(businessKey));
			leave.setTask(task);
			leave.setProcessInstance(processInstance);
			leave.setProcessDefinition(getProcessDefinition(processInstance.getProcessDefinitionId()));
			results.add(leave);
		}
		if (results.size() > 0) {
			editLeaveResults(results, leavePK.toString(), currentpage, pagesize);
		}
		return results;
	}

	/** 编辑待办任务的结果集 */
	private void editLeaveResults(List<Leave> results, String pkArray, int currentpage, int pagesize) {
		PageResult<Leave> res = oaLeaveDao.listLeaves(currentpage, pagesize, pkArray);
		for (Leave leave : results) {
			for (Leave dl : res.getArray()) {
				if (leave.getId() == dl.getId()) {
					leave.setApplyTime(dl.getApplyTime());
					leave.setEndTime(dl.getEndTime());
					leave.setStartTime(dl.getStartTime());
					leave.setLeaveType(dl.getLeaveType());
					leave.setProcessInstanceId(dl.getProcessInstanceId());
					leave.setReason(dl.getReason());
					leave.setUserId(dl.getUserId());
					break;// TODO;liuxing为什么break
				}
			}
		}

	}
	/**
	 * 查询流程定义对象
	 *
	 * @param processDefinitionId
	 *            流程定义ID
	 * @return
	 */
	protected ProcessDefinition getProcessDefinition(String processDefinitionId) {
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(processDefinitionId).singleResult();
		return processDefinition;
	}

	// 完成任务
	public void completeTasks(Boolean joinApproved, String taskId) {
		Map<String, Object> taskVariables = new HashMap<String, Object>();
		taskVariables.put("joinApproved", joinApproved);
		taskService.complete(taskId, taskVariables);
	}
}

package com.chenxing.managesystem.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
import com.chenxing.common.util.PageUtil;
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
	public PageResult<Leave> getTasks(String assignee, long currentpage, long pagesize) {
		PageResult<Leave> resRtn = new PageResult<Leave>();
		TaskQuery taskQuery = taskService.createTaskQuery().taskCandidateOrAssigned(assignee);
		resRtn.setTotalCount(taskQuery.count());
		PageUtil pageUtil = new PageUtil(pagesize, currentpage, resRtn.getTotalCount());
		resRtn.setTotalPage(pageUtil.getPageCount());
		List<Task> tasks = taskQuery.orderByTaskCreateTime().desc().listPage(pageUtil.getFirstResult().intValue(),
				pageUtil.getPageSize().intValue());
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

			Leave leave = new Leave();
			leave.setId(Long.parseLong(businessKey));

			Map<String, Object> taskmap = new LinkedHashMap<>();
			taskmap.put("taskId", task.getId());
			taskmap.put("taskDefinitionKey", task.getTaskDefinitionKey());
			taskmap.put("taskName", task.getName());
			taskmap.put("createTime", task.getCreateTime());
			taskmap.put("assignee", task.getAssignee());
			leave.setTask(taskmap);
			
			
			Map<String, Object> processInstanceMap = new LinkedHashMap<>();
			processInstanceMap.put("id", processInstance.getId());
			processInstanceMap.put("processDefinitionId", processInstance.getProcessDefinitionId());
			processInstanceMap.put("name", processInstance.getName());
			processInstanceMap.put("suspended", processInstance.isSuspended());
			leave.setProcessInstanceMap(processInstanceMap);
			Leave dl = oaLeaveDao.findById(businessKey);
			if (dl != null) {
				leave.setApplyTime(dl.getApplyTime());
				leave.setEndTime(dl.getEndTime());
				leave.setStartTime(dl.getStartTime());
				leave.setLeaveType(dl.getLeaveType());
				leave.setProcessInstanceId(dl.getProcessInstanceId());
				leave.setReason(dl.getReason());
				leave.setUserId(dl.getUserId());
			}
			leave.setProcessDefinitionMap(getProcessDefinition(processInstance.getProcessDefinitionId()));
			results.add(leave);
		}
		resRtn.setArray(results);
		return resRtn;
	}

	/**
	 * 查询流程定义对象
	 *
	 * @param processDefinitionId
	 *            流程定义ID
	 * @return
	 */
	protected Map<String, Object> getProcessDefinition(String processDefinitionId) {
		Map<String, Object> map = new LinkedHashMap<>();
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(processDefinitionId).singleResult();
		map.put("version", processDefinition.getVersion());
		return map;
	}

	/**
	 * 执行任务
	 *
	 * @param id
	 * @return
	 */
	public String complete(String taskId, Map<String, Object> var) {
		taskService.complete(taskId, var);
		return "success";
	}

}

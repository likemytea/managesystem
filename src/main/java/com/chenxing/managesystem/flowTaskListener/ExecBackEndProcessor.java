package com.chenxing.managesystem.flowTaskListener;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.runtime.ProcessInstance;
import org.assertj.core.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chenxing.managesystem.dao.OaLeaveDao;
import com.chenxing.managesystem.domain.Leave;

/**
 * 销假触发的处理器
 * <p>
 * 设置销假时间
 * </p>
 * <p>
 * 使用Spring代理，可以注入Bean，管理事务
 * </p>
 *
 * @author xing.liu
 */
@Component
@Transactional
public class ExecBackEndProcessor implements TaskListener {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private static final long serialVersionUID = 5598598670532196624L;

	@Autowired
	private OaLeaveDao oaLeaveDao;

	@Autowired
	RuntimeService runtimeService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.activiti.engine.delegate.TaskListener#notify(org.activiti.engine.delegate
	 * .DelegateTask)
	 */
	public void notify(DelegateTask delegateTask) {
		String processInstanceId = delegateTask.getProcessInstanceId();
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
				.processInstanceId(processInstanceId).singleResult();
		Leave leave = oaLeaveDao.findById(processInstance.getBusinessKey());

		Object remarks = delegateTask.getVariable("请假原因");
		log.info("remarks:" + remarks);
		leave.setRealityEndTime(DateUtil.now());
		oaLeaveDao.insertOaLeave(leave);
	}
}

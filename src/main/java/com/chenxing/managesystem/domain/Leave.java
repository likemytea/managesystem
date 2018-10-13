package com.chenxing.managesystem.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import org.activiti.engine.history.HistoricProcessInstance;
import org.springframework.format.annotation.DateTimeFormat;

import com.chenxing.managesystem.domain.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Entity: Leave
 *
 * @author xing.liu
 */
@JsonIgnoreProperties({ "handler", "hibernateLazyInitializer" })
public class Leave extends IdEntity implements Serializable {

	private static final long serialVersionUID = 3182317672326236654L;
	private String processInstanceId;
    private String userId;

    private String startTime;

    private String endTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date realityStartTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date realityEndTime;
    private Date applyTime;
    private String leaveType;
    private String reason;

    //-- 临时属性 --//

    // 流程任务
	private Map<String, Object> task;

    private Map<String, Object> variables;

    // 运行中的流程实例
	private Map<String, Object> processInstanceMap;

    // 历史的流程实例
    private HistoricProcessInstance historicProcessInstance;

    // 流程定义
	private Map<String, Object> processDefinitionMap;

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getRealityStartTime() {
        return realityStartTime;
    }

    public void setRealityStartTime(Date realityStartTime) {
        this.realityStartTime = realityStartTime;
    }

    public Date getRealityEndTime() {
        return realityEndTime;
    }

    public void setRealityEndTime(Date realityEndTime) {
        this.realityEndTime = realityEndTime;
    }

	public Map<String, Object> getTask() {
		return task;
	}

	public void setTask(Map<String, Object> task) {
		this.task = task;
	}

	public Map<String, Object> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }



	public Map<String, Object> getProcessInstanceMap() {
		return processInstanceMap;
	}

	public void setProcessInstanceMap(Map<String, Object> processInstanceMap) {
		this.processInstanceMap = processInstanceMap;
	}

	public HistoricProcessInstance getHistoricProcessInstance() {
        return historicProcessInstance;
    }

    public void setHistoricProcessInstance(HistoricProcessInstance historicProcessInstance) {
        this.historicProcessInstance = historicProcessInstance;
    }

	public Map<String, Object> getProcessDefinitionMap() {
		return processDefinitionMap;
	}

	public void setProcessDefinitionMap(Map<String, Object> processDefinitionMap) {
		this.processDefinitionMap = processDefinitionMap;
	}


}

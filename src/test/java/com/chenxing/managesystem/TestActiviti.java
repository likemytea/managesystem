package com.chenxing.managesystem;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.chenxing.common.vo.PageResult;
import com.chenxing.managesystem.domain.Leave;
import com.chenxing.managesystem.service.OaLeaveWorkFlowService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TestActiviti {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	OaLeaveWorkFlowService oaLeaveWorkFlowService;

	// @Test
	public void TestStartProcess() {
		log.info("###########开始测试启动流程");
		// Create Table
		//
		// CREATE TABLE `oa_leave` (
		// `id` bigint(20) NOT NULL AUTO_INCREMENT,
		// `apply_time` datetime DEFAULT NULL,
		// `end_time` varchar(128) DEFAULT NULL,
		// `leave_type` varchar(255) DEFAULT NULL,
		// `process_instance_id` varchar(255) DEFAULT NULL,
		// `reality_end_time` datetime DEFAULT NULL,
		// `reality_start_time` datetime DEFAULT NULL,
		// `reason` varchar(255) DEFAULT NULL,
		// `start_time` varchar(128) DEFAULT NULL,
		// `user_id` varchar(255) DEFAULT NULL,
		// PRIMARY KEY (`id`)
		// ) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8
		Leave l = new Leave();
		l.setApplyTime(new Date());
		l.setEndTime("2050-07-29 00:00:00");
		l.setLeaveType("公休");
		// l.setProcessInstanceId(processInstanceId);
		l.setRealityEndTime(new Date());
		l.setRealityStartTime(new Date());
		l.setReason("junit测试哈");
		l.setStartTime("2030-07-29 00:00:00");
		l.setUserId("admin");

		try {
			oaLeaveWorkFlowService.startProcess(l);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testGetTasks() {
		try {

			log.info("###########开始测试获取任务列表");
			String assignee = "admin";
			PageResult<Leave> rtn = oaLeaveWorkFlowService.getTasks(assignee, 1, 10);
			log.info("++++++++++++++++++++++");
			for (Leave l : rtn.getArray()) {
				log.info("id:" + l.getId());
				log.info("reason:" + l.getReason());
				log.info("userid:" + l.getUserId());
				log.info("starttime:" + l.getStartTime());
				log.info("getProcessInstance.id:" + l.getProcessInstanceMap().get("id"));
				log.info("getProcessInstance.name:" + l.getProcessInstanceMap().get("name"));
				log.info("getProcessDefinition.version:" + l.getProcessDefinitionMap().get("version"));
				log.info("  |");
			}
			log.info("++++++++++++++++++++++end");

		} catch (Exception e) {
			e.printStackTrace();
		}
		}
}

package com.chenxing.managesystem;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.chenxing.managesystem.domain.Leave;
import com.chenxing.managesystem.service.OaLeaveWorkFlowService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TestActiviti {
	@Autowired
	OaLeaveWorkFlowService runtimeService;

	@Test
	public void TestStartProcess() {
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
		l.setId(7l);
		l.setApplyTime(new Date());
		l.setEndTime("2050-07-29 00:00:00");
		l.setLeaveType("公休");
		// l.setProcessInstanceId(processInstanceId);
		l.setRealityEndTime(new Date());
		l.setRealityStartTime(new Date());
		l.setReason("junit测试data");
		l.setStartTime("2030-07-29 00:00:00");
		l.setUserId("admin");

		runtimeService.startProcess(l);
	}
}

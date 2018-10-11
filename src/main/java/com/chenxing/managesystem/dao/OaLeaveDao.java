package com.chenxing.managesystem.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.chenxing.common.jdbc.MyJdbcTemplate;
import com.chenxing.managesystem.domain.Leave;

/**
 * Description:
 * 
 * @author liuxing
 * @date 2018年10月11日
 * @version 1.0
 */
@Repository
public class OaLeaveDao {

	@Autowired
	@Qualifier("myJdbcTemplatep3")
	private MyJdbcTemplate jdbcTemplate;

	public int insertOaLeave(Leave lv) {

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

		return jdbcTemplate.update("INSERT INTO oa_leave VALUES(?,?,?,?,?,?,?,?,?,?)",
				new Object[] { lv.getId(), lv.getApplyTime(), lv.getEndTime(), lv.getLeaveType(),
						lv.getProcessInstanceId(), lv.getRealityEndTime(), lv.getRealityStartTime(), lv.getReason(),
						lv.getStartTime(), lv.getUserId() });
	}

}
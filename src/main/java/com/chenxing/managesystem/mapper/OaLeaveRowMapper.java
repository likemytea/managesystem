package com.chenxing.managesystem.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.chenxing.managesystem.domain.Leave;

/**
 * liuxing 2018.10.11
 */
public class OaLeaveRowMapper implements RowMapper<Leave> {

	@Override
	public Leave mapRow(ResultSet rs, int rowNum) throws SQLException {
		Leave l = new Leave();
		l.setId(rs.getLong("id"));
		l.setApplyTime(rs.getDate("apply_time"));
		l.setEndTime(rs.getString("end_time"));
		l.setLeaveType(rs.getString("leave_type"));
		l.setProcessInstanceId(rs.getString("process_instance_id"));
		l.setRealityEndTime(rs.getDate("reality_end_time"));
		l.setRealityStartTime(rs.getDate("reality_start_time"));
		l.setReason(rs.getString("reason"));
		l.setStartTime(rs.getString("start_time"));
		l.setUserId(rs.getString("user_id"));
		return l;
	}

}

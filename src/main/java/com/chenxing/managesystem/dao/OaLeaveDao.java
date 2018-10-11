package com.chenxing.managesystem.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.chenxing.common.jdbc.MyJdbcTemplate;
import com.chenxing.common.pagination.PaginationResult;
import com.chenxing.common.pagination.SortType;
import com.chenxing.common.vo.PageResult;
import com.chenxing.managesystem.domain.Leave;
import com.chenxing.managesystem.mapper.OaLeaveRowMapper;

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

	/**
	 * save 实体
	 */
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

	/**
	 * 查询实体，返回list
	 */
	public PageResult<Leave> listLeaves(int currentpage, int pagesize, String pkArray) {
		StringBuffer sql = new StringBuffer("SELECT * from oa_leave u where u.id in(");
		sql.append(pkArray.substring(0, pkArray.length() - 1));
		sql.append(")");

		PaginationResult<Leave> res = jdbcTemplate.queryForPage(sql.toString(), currentpage, pagesize,
				"apply_time",
				SortType.DESC,
				new OaLeaveRowMapper());

		PageResult<Leave> pr = new PageResult<Leave>();
		pr.setArray(res.getData());
		pr.setTotalCount(res.getTotalCount());
		pr.setTotalPage(res.getTotalPage());
		return pr;
	}
}
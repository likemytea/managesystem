package com.chenxing.managesystem.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.chenxing.managesystem.domain.SysRole;
import com.chenxing.managesystem.domain.SysUser;

/**  
* Description:
* @author liuxing
* @date 2018年5月14日  
* @version 1.0  
*/
@Repository
public class UserDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;


	public SysUser findByUserName(String username) {

		String rsql = "SELECT u.id,u.username,u.password,r.name from sys_user u "
				+ "left join sys_role_user sru on u.id= sru.sys_user_id "
				+ "LEFT JOIN sys_role r on sru.sys_role_id=r.id where username= ?";

		List<Map<String, String>> ms = jdbcTemplate.query(rsql, new RowMapper<Map<String, String>>() {
			@Override
			public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
				Map<String, String> p = new HashMap<String, String>();
				p.put("userid", String.valueOf(rs.getInt(1)));
				p.put("username", rs.getString(2));
				p.put("password", rs.getString(3));
				p.put("rolename", rs.getString(4));
				return p;
			}
		}, username);
		return map2Obj(ms);
	}

	private SysUser map2Obj(List<Map<String, String>> ms) {
		SysUser p = new SysUser();
		SysRole sysRole = null;
		List<SysRole> roles = new ArrayList<SysRole>();
		for (Map<String, String> map : ms) {
			sysRole = new SysRole();
			p.setId(Integer.parseInt(map.get("userid")));
			p.setUsername(map.get("username"));
			p.setPassword(map.get("password"));
			sysRole.setName(map.get("rolename"));
			roles.add(sysRole);
		}
		p.setRoles(roles);
		return p;
	}

}
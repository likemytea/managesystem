package com.chenxing.managesystem.service;

import java.util.List;

import com.chenxing.managesystem.domain.User;

/**
 * mybatis demo
 * 
 */
public interface UserService {
	int addUser(User user);

	List<User> findAllUser(int pageNum, int pageSize);
}

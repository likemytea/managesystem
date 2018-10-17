package com.chenxing.managesystem.mapper;

import java.util.List;

import com.chenxing.managesystem.domain.User;

public interface UserMapper {
	int deleteByPrimaryKey(Integer userId);

	int insert(User record);

	int insertSelective(User record);

	User selectByPrimaryKey(Integer userId);

	int updateByPrimaryKeySelective(User record);

	int updateByPrimaryKey(User record); 
	// 这个方式我自己加的
	List<User> selectAllUser();
}

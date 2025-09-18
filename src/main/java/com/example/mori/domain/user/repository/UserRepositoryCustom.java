package com.example.mori.domain.user.repository;

import java.util.List;

import com.example.mori.domain.user.dto.request.UserSearchRequest;
import com.example.mori.domain.user.entity.User;

public interface UserRepositoryCustom {
	List<User> search(UserSearchRequest request);
	long count(UserSearchRequest request);
}

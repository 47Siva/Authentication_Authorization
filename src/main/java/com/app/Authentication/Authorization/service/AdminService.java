package com.app.Authentication.Authorization.service;

import org.springframework.stereotype.Service;

import com.app.Authentication.Authorization.entity.User;
import com.app.Authentication.Authorization.enumeration.Role;
import com.app.Authentication.Authorization.enumeration.UserStatus;
import com.app.Authentication.Authorization.repository.UserRepository;
import com.app.Authentication.Authorization.util.PasswordUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {
	
	private final UserRepository userRepository;

	public User createAdmin(User user) {
		var adminobj = User.builder()
				          .email(user.getEmail())
				          .mobileNo(user.getMobileNo())
				          .password(PasswordUtil.getEncryptedPassword(user.getPassword()))
				          .role(Role.ADMIN)
				          .status(UserStatus.ACTIVE).userName(user.getUsername()).build();
		return userRepository.saveAndFlush(adminobj);
	}
	
}

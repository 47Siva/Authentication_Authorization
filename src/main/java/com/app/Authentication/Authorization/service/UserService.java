package com.app.Authentication.Authorization.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.app.Authentication.Authorization.entity.User;
import com.app.Authentication.Authorization.enumeration.Role;
import com.app.Authentication.Authorization.enumeration.UserStatus;
import com.app.Authentication.Authorization.repository.UserRepository;
import com.app.Authentication.Authorization.util.PasswordUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	public User createUser(User user) {
		var userobj = User.builder().email(user.getEmail()).mobileNo(user.getMobileNo())
				.password(PasswordUtil.getEncryptedPassword(user.getPassword())).role(Role.USER)
				.status(UserStatus.ACTIVE).userName(user.getUsername()).build();

		return userRepository.saveAndFlush(userobj);
	}

	public Optional<User> findByUserName(String userName) {
		return userRepository.findByUserName(userName);
	}

	public Optional<User> findByDuplicateEmail(String email) {
		
		return userRepository.findByDuplicateEamil(email);
	}

	public Optional<User> findByDuplicateUserName(String userName) {
		return userRepository.findByDuplicateUserName(userName);
	}

	public Optional<User> getMobileNos(String phoneNumber) {
		return userRepository.findByDuplicateNumber(phoneNumber);
	}

	public Optional<User> findByDuplicatePassword(String password) {
		return userRepository.findByDuplicatePassword(password);
	}
}

package com.app.Authentication.Authorization.response;

import com.app.Authentication.Authorization.entity.User;

public class UserMapper {
	
    public static UserResponse toUserResponse(User user) {
        return new UserResponse(
            user.getUsername(),
            user.getEmail(),
            user.getMobileNo(),
            user.getStatus()
        );
    }
}

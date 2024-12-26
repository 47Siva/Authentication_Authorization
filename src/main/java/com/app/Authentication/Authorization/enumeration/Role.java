package com.app.Authentication.Authorization.enumeration;

import com.app.Authentication.Authorization.advice.HttpMessageNotReadableException;

public enum Role {
	
	ADMIN,USER,CUSTOMER;
	
	  public static Role fromString(String value) {
        for (Role role : Role.values()) {
            if (role.name().equalsIgnoreCase(value)) {
                return role;
            }
        }
//        throw new IllegalArgumentException("Invalid role: " + value);
        throw new HttpMessageNotReadableException("Invalid role: " + value);
    }

}

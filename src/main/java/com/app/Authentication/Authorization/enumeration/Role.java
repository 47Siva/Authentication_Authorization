package com.app.Authentication.Authorization.enumeration;

public enum Role {
	
	ADMIN,USER;
	
	  public static Role fromString(String value) {
        for (Role role : Role.values()) {
            if (role.name().equalsIgnoreCase(value)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid role: " + value);
    }

}

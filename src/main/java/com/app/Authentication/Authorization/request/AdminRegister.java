package com.app.Authentication.Authorization.request;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminRegister {
    private String userName;
    private String email;
    private String mobileNo;
    private String password;
}

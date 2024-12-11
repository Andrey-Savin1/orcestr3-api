package com.example.orcestr3api.dto;

import lombok.Data;

@Data
public class UserRegistrationRequest {

    private String email;
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private String token;
}

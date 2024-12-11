package com.example.orcestr3api.dto;

import lombok.Data;

@Data
public class UserInfo {

    private String id;
    private String email;
    private String error;
    private String status;
    private String createdAt;

}

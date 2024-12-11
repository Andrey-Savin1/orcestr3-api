package com.example.orcestr3api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccessTokenResponse {

   private String accessToken;
   private String expiresIn;
   private String refreshToken;
   private String tokenType;
   private String error;
   private String status;
   private String errorDetails;

}

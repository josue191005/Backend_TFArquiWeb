package com.upc.staycool.DTOs;

import lombok.Data;

@Data
public class LoginReqDTO {
    private String email;
    private String password;
}
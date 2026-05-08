package com.upc.trabajoparcial.DTOs;

import lombok.Data;

@Data
public class LoginReqDTO {
    private String email;
    private String password;
}
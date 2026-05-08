package com.upc.trabajoparcial.DTOs;

import lombok.Data;

@Data
public class RegistroReqDTO {
    private String name;
    private String email;
    private String password;
}
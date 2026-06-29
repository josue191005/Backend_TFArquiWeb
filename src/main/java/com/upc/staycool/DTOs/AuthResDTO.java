package com.upc.staycool.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResDTO {
    private String token;
    private Long userId;
    private String userName;
}
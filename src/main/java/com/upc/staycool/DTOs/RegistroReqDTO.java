package com.upc.staycool.DTOs;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class RegistroReqDTO {
    private String name;
    private String email;
    private String password;
    private Integer age;
    private Long rolId;
    private String specialty;
    private String clinicName;
}
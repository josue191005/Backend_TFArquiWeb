package com.upc.staycool.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioPasswordDTO {
    private String currentPassword;
    private String newPassword;
}

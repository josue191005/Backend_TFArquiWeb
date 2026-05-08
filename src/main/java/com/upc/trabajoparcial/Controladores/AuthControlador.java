package com.upc.trabajoparcial.Controladores;

import com.upc.trabajoparcial.DTOs.AuthResDTO;
import com.upc.trabajoparcial.DTOs.LoginReqDTO;
import com.upc.trabajoparcial.DTOs.RegistroReqDTO;
import com.upc.trabajoparcial.Servicios.AuthServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthControlador {

    @Autowired
    private AuthServicio authServicio;

    // US01: Registrarme en la App
    @PostMapping("/register")
    public ResponseEntity<AuthResDTO> registrar(@RequestBody RegistroReqDTO dto) {
        return ResponseEntity.ok(authServicio.registrar(dto));
    }

    // US20: Iniciar sesión
    @PostMapping("/login")
    public ResponseEntity<AuthResDTO> login(@RequestBody LoginReqDTO dto) {
        return ResponseEntity.ok(authServicio.login(dto));
    }

    // US24: Cerrar sesión
    @PostMapping("/logout")
    public org.springframework.http.ResponseEntity<java.util.Map<String, String>> logout() {
        // En arquitecturas stateless (JWT), el borrado real del token ocurre en el Frontend.
        // Aquí devuelve la confirmación de éxito.
        java.util.Map<String, String> response = new java.util.HashMap<>();
        response.put("mensaje", "Sesión cerrada exitosamente. El cliente debe desechar el token.");

        return org.springframework.http.ResponseEntity.ok(response);
    }
}
package com.upc.trabajoparcial.Servicios;

import com.upc.trabajoparcial.DTOs.AuthResDTO;
import com.upc.trabajoparcial.DTOs.LoginReqDTO;
import com.upc.trabajoparcial.DTOs.RegistroReqDTO;
import com.upc.trabajoparcial.Entidades.UsuarioEntidad;
import com.upc.trabajoparcial.Repositorios.UsuarioRepositorio;
import com.upc.trabajoparcial.Seguridad.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServicio {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    public AuthResDTO registrar(RegistroReqDTO dto) {
        UsuarioEntidad nuevoUsuario = new UsuarioEntidad();
        nuevoUsuario.setName(dto.getName());
        nuevoUsuario.setEmail(dto.getEmail());

        // ¡IMPORTANTE! Encriptamos la contraseña antes de guardarla en la base de datos
        nuevoUsuario.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        nuevoUsuario.setTotalPoints(0);

        usuarioRepositorio.save(nuevoUsuario);

        // Generamos el token inmediatamente después de registrarse
        UserDetails userDetails = userDetailsService.loadUserByUsername(nuevoUsuario.getEmail());
        String token = jwtUtil.generarToken(userDetails);

        return new AuthResDTO(token);
    }

    public AuthResDTO login(LoginReqDTO dto) {
        // Spring Security verifica que la contraseña enviada coincida con el hash de la BD
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );

        // Si la autenticación es exitosa, generamos y devolvemos el token
        UserDetails userDetails = userDetailsService.loadUserByUsername(dto.getEmail());
        String token = jwtUtil.generarToken(userDetails);

        return new AuthResDTO(token);
    }
}
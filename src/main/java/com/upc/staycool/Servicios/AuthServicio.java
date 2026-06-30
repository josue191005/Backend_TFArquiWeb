package com.upc.staycool.Servicios;

import com.upc.staycool.DTOs.AuthResDTO;
import com.upc.staycool.DTOs.LoginReqDTO;
import com.upc.staycool.DTOs.RegistroReqDTO;
import com.upc.staycool.Entidades.RolEntidad;
import com.upc.staycool.Entidades.UsuarioEntidad;
import com.upc.staycool.Repositorios.RolRepositorio;
import com.upc.staycool.Repositorios.UsuarioRepositorio;
import com.upc.staycool.Seguridad.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServicio {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private RolRepositorio rolRepositorio;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Transactional
    public AuthResDTO registrar(RegistroReqDTO dto) {
        // 1. Validar si el correo ya existe
        if (usuarioRepositorio.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Error: El correo electrÃ³nico ya estÃ¡ registrado.");
        }

        UsuarioEntidad nuevoUsuario = new UsuarioEntidad();
        nuevoUsuario.setName(dto.getName());
        nuevoUsuario.setEmail(dto.getEmail());
        nuevoUsuario.setTotalPoints(0);
        
        if (dto.getAge() != null) {
            nuevoUsuario.setAge(dto.getAge());
        }
        
        // Guardar datos adicionales de psicólogo si se envían
        if (dto.getSpecialty() != null) {
            nuevoUsuario.setSpecialty(dto.getSpecialty());
        }
        if (dto.getClinicName() != null) {
            nuevoUsuario.setClinicName(dto.getClinicName());
        }

        // 2. Encriptamos la contraseÃ±a de forma segura
        nuevoUsuario.setPasswordHash(passwordEncoder.encode(dto.getPassword()));

        // 3. AsignaciÃ³n de Rol Inteligente (Busca por nombre, no por ID)
        // LÃ³gica: Si mandan rolId = 2 en el JSON, es PsicÃ³logo. Si no mandan nada o mandan 1, es Paciente.
        String nombreABuscar = (dto.getRolId() != null && dto.getRolId() == 2L) ? "PSICOLOGO" : "PACIENTE";

        RolEntidad rolAsignado = rolRepositorio.findByName(nombreABuscar)
                .orElseThrow(() -> new RuntimeException("Error crÃ­tico: El rol " + nombreABuscar + " no existe. Revisa el DataInitializer."));

        nuevoUsuario.setRolEntidad(rolAsignado);

        // 4. Guardar usuario en la base de datos
        usuarioRepositorio.save(nuevoUsuario);

        // 5. Generar Token inmediatamente
        UserDetails userDetails = userDetailsService.loadUserByUsername(nuevoUsuario.getEmail());
        String token = jwtUtil.generarToken(userDetails);

        return new AuthResDTO(token, nuevoUsuario.getId(), nuevoUsuario.getName());
    }

    public AuthResDTO login(LoginReqDTO dto) {
        // VerificaciÃ³n de credenciales con Spring Security
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(dto.getEmail());
        String token = jwtUtil.generarToken(userDetails);

        UsuarioEntidad usuario = usuarioRepositorio.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return new AuthResDTO(token, usuario.getId(), usuario.getName());
    }
}
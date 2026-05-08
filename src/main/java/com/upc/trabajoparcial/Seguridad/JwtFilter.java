package com.upc.trabajoparcial.Seguridad;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    // Aquí llamaremos al servicio que conecta la base de datos de tu compañero Josué
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 1. Revisamos si en la petición (Postman/Frontend) viene el token en la cabecera "Authorization"
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        // 2. Verificamos que el token exista y empiece con la palabra clave "Bearer "
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7); // Cortamos los primeros 7 caracteres ("Bearer ")
            try {
                username = jwtUtil.extraerUsername(jwt);
            } catch (Exception e) {
                System.out.println("Error: Token inválido o ha expirado.");
            }
        }

        // 3. Si encontramos un usuario en el token y no ha iniciado sesión en este request...
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // Validamos que el token corresponda a ese usuario y no esté vencido
            if (jwtUtil.validarToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Le damos el pase libre ("Autenticado") para que pase al Controlador
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        // 4. Dejamos que la petición siga su camino
        filterChain.doFilter(request, response);
    }
}
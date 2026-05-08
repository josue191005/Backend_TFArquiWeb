package com.upc.trabajoparcial.Seguridad;

import com.upc.trabajoparcial.Entidades.UsuarioEntidad;
import com.upc.trabajoparcial.Repositorios.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. Buscamos al usuario usando el método nuevo (el 'username' en realidad será el correo del usuario)
        UsuarioEntidad usuario = usuarioRepositorio.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con el correo: " + username));

        // 2. Traducimos tu UsuarioEntidad usando los getters correctos de Josué
        return new User(
                usuario.getEmail(),        // ¡Corregido!
                usuario.getPasswordHash(), // ¡Corregido!
                new ArrayList<>()
        );
    }
}
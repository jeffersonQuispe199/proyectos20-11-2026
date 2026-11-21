package com.krakedev.proyectos.services;

import com.krakedev.proyectos.entidades.Usuario;
import com.krakedev.proyectos.repositories.UsuarioRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario registrarUsuario(Usuario usuario) {
        if (usuarioRepository.findByUsername(usuario.getUsername()).isPresent()) {
            throw new RuntimeException("El nombre de usuario ya existe");
        }
        // Hasheo unidireccional seguro con BCrypt
        String passwordHasheada = BCrypt.hashpw(usuario.getPassword(), BCrypt.gensalt(10));
        usuario.setPassword(passwordHasheada);
        usuario.setRol(usuario.getRol().toUpperCase());
        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> autenticar(String username, String passwordPlano) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(username);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            // Validación segura con BCrypt.checkpw (nunca .equals())
            if (BCrypt.checkpw(passwordPlano, usuario.getPassword())) {
                return Optional.of(usuario);
            }
        }
        return Optional.empty();
    }

    public Optional<Usuario> buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }
}
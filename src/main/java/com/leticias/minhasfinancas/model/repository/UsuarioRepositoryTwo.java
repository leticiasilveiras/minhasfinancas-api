package com.leticias.minhasfinancas.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.leticias.minhasfinancas.model.entity.Usuario;

public interface UsuarioRepositoryTwo extends JpaRepository<Usuario, Long> {

	Optional<Usuario> findByEmail(String email);

	boolean existsByEmail(String anyString);
}

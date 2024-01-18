package com.leticias.minhasfinancas.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.leticias.minhasfinancas.exception.ErroAutenticacao;
import com.leticias.minhasfinancas.exception.RegraNegocioException;
import com.leticias.minhasfinancas.model.entity.Usuario;
import com.leticias.minhasfinancas.model.repository.UsuarioRepositoryTwo;
import com.leticias.minhasfinancas.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService{
	
	
	private UsuarioRepositoryTwo repository;
	
	public UsuarioServiceImpl(UsuarioRepositoryTwo repository) {
		super();
		this.repository = repository;
	}

	@Override
	public Usuario autenticar(String email, String senha) {
		Optional<Usuario> usuario =	repository.findByEmail(email);
		
		if(!usuario.isPresent()) {
			throw new ErroAutenticacao("Usuário não encontrado para o email informado.");
		}
		
		if(!usuario.get().getSenha().equals(senha)) {
			throw new ErroAutenticacao("Senha Inválida.");
		}
		
		return usuario.get();
		
	}

	@Override
	@Transactional
	public Usuario salvarUsuario(Usuario usuario) {
		validarEmail(usuario.getEmail());
		return repository.save(usuario);
	}

	@Override
	public void validarEmail(String email) {
		boolean existe = repository.existsByEmail(email);
		if(existe ) {
			throw new RegraNegocioException("Já existe um usuário cadastrado com este email.");
		}
	}

}

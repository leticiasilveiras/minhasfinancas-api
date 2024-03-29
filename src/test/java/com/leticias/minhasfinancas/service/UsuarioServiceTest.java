package com.leticias.minhasfinancas.service;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.leticias.minhasfinancas.exception.ErroAutenticacao;
import com.leticias.minhasfinancas.exception.RegraNegocioException;
import com.leticias.minhasfinancas.model.entity.Usuario;
import com.leticias.minhasfinancas.model.repository.UsuarioRepositoryTwo;
import com.leticias.minhasfinancas.service.impl.UsuarioServiceImpl;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {
	
	@SpyBean
	UsuarioServiceImpl service;
	
	@MockBean
	private UsuarioRepositoryTwo repository;
	
	@Test
	public void deveSalvarUmUsuario() {
		Assertions.assertDoesNotThrow(() -> {
			//cenario
			Mockito.doNothing().when(service).validarEmail(Mockito.anyString());
			Usuario usuario = Usuario.builder()
					.id(1l)
					.nome("nome")
					.email("email@email.com")
					.senha("senha")	.build();
			Mockito.when(repository.save(Mockito.any(Usuario.class))).thenReturn(usuario);
			
			//acao
			Usuario usuarioSalvo = service.salvarUsuario(new Usuario());
			
			//verificacao
			org.assertj.core.api.Assertions.assertThat(usuarioSalvo).isNotNull();
			org.assertj.core.api.Assertions.assertThat(usuarioSalvo.getId()).isEqualTo(1l);
			org.assertj.core.api.Assertions.assertThat(usuarioSalvo.getNome()).isEqualTo("nome");
			org.assertj.core.api.Assertions.assertThat(usuarioSalvo.getEmail()).isEqualTo("email@email.com");
			org.assertj.core.api.Assertions.assertThat(usuarioSalvo.getSenha()).isEqualTo("senha");
		});
	}
	
	@Test
	public void naoDeveSalvarUmUsuarioComEmailJaCadastrado() {
		Assertions.assertThrows(RegraNegocioException.class, () -> {
		//cenario
		String email = "email@email.com";
		
		Usuario usuario = Usuario.builder().email(email).build();
		Mockito.doThrow(RegraNegocioException.class).when(service).validarEmail(email);
		
		//acao
		service.salvarUsuario(usuario);
		
		//verificacao
		Mockito.verify(repository, Mockito.never()).save(usuario);
		});
	}
	
	@Test
	public void deveAutenticarUmUsuarioComSucesso() {
		Assertions.assertDoesNotThrow(() -> {
		//cenario
		String email = "email@email.com";
		String senha = "senha";
	
		Usuario usuario = Usuario.builder().email(email).senha(senha).id(1l).build();
		Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(usuario));
		
	
		//acao
		Usuario result = service.autenticar(email, senha);
		
		//verificacao
		org.assertj.core.api.Assertions.assertThat(result).isNotNull();
		});
		
	}
	
	@Test
	public void deveLancarErroQuandoNaoEncontarUsuarioCadastradoComOEmailInformado() {
			//cenario
			Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
			
			//acao
			Throwable exception = Assertions.assertThrows(ErroAutenticacao.class, () -> service.autenticar("email@email.com", "senha"));		
			
			//verificacao
			org.assertj.core.api.Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class).hasMessage("Usuário não encontrado para o email informado.");
	}
	
	@Test
	public void deveLancarErroQuandoASenhaNaoBater() {
			//cenario
			String senha = "senha";
			Usuario usuario = Usuario.builder().email("email@email.com").senha(senha).build();
			Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));
			
			//acao
			Throwable exception = Assertions.assertThrows(ErroAutenticacao.class, () -> service.autenticar("email@email.com", "123"));	
			
			//verificacao
			org.assertj.core.api.Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class).hasMessage("Senha Inválida.");
	}
	
	@Test
	public void deveValidarEmail() {
		Assertions.assertDoesNotThrow(() -> {
			//cenario
			Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);
			
			//acao
			service.validarEmail("email@email.com");
			
		});
	}
	
	@Test
	public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado() {
		Assertions.assertThrows(RegraNegocioException.class, () -> {
			//cenario
			Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);
			
			//acao
			service.validarEmail("email@email.com");
		});
	}

}

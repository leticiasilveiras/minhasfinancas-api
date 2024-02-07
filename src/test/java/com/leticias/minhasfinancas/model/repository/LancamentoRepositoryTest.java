package com.leticias.minhasfinancas.model.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.leticias.minhasfinancas.model.entity.Lançamentos;
import com.leticias.minhasfinancas.model.enums.StatusLancamento;
import com.leticias.minhasfinancas.model.enums.TipoLancamento;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class LancamentoRepositoryTest {

	@Autowired
	LancamentoRepository repository;
	
	@Autowired
	TestEntityManager entityManager;
	
	@Test
	public void deveSalvarUmLancamento() {
		Lançamentos lancamento = criarLancamento();
		
		lancamento = repository.save(lancamento);
		
		assertThat(lancamento.getId()).isNotNull();
	}
	
	@Test
	public void deveDeletarUmLancamento() {
		Lançamentos lancamento = criarEPersistirUmLancamento();
		
		lancamento = entityManager.find(Lançamentos.class, lancamento.getId());
		
		repository.delete(lancamento);
		
		Lançamentos lancamentoInexistente = entityManager.find(Lançamentos.class, lancamento.getId());
		assertThat(lancamentoInexistente).isNull();
	}
	
	@Test
	public void deveAtualizarUmLancamento() {
		Lançamentos lancamento = criarEPersistirUmLancamento();
		
		lancamento.setAno(2023);
		lancamento.setDescricao("Teste atualizar");
		lancamento.setStatus(StatusLancamento.CANCELADO);
		
		repository.save(lancamento);
		
		Lançamentos lancamentoAtualizado = entityManager.find(Lançamentos.class, lancamento.getId());
		
		assertThat(lancamentoAtualizado.getAno()).isEqualTo(2023);
		assertThat(lancamentoAtualizado.getDescricao()).isEqualTo("Teste atualizar");
		assertThat(lancamentoAtualizado.getStatus()).isEqualTo(StatusLancamento.CANCELADO);
	}
	
	@Test
	public void deveBuscarUmLancamentoPorId() {
		Lançamentos lancamento = criarEPersistirUmLancamento();
	
		Optional<Lançamentos> lancamentoEncontrado = repository.findById(lancamento.getId());
		
		assertThat(lancamentoEncontrado.isPresent()).isTrue();
	}
	
	private Lançamentos criarEPersistirUmLancamento() {
		Lançamentos lancamento = criarLancamento();
		entityManager.persist(lancamento);
		return lancamento;
	}
	
	public static Lançamentos criarLancamento() {
		return Lançamentos.builder()
									.ano(2024)
									.mes(1)
									.descricao("Lançamento qualquer")
									.valor(BigDecimal.valueOf(10))
									.tipo(TipoLancamento.RECEITA)
									.status(StatusLancamento.PENDENTE)
									.dataCadastro(LocalDate.now())
									.build();
	}
}

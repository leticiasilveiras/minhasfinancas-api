package com.leticias.minhasfinancas.service;

import java.util.List;

import com.leticias.minhasfinancas.model.entity.Lançamentos;
import com.leticias.minhasfinancas.model.enums.StatusLancamento;

public interface LancamentoService {
	Lançamentos salvar(Lançamentos lancamento);
	
	Lançamentos atualizar(Lançamentos lancamento);
	
	void deletar(Lançamentos lancamento);
	
	List<Lançamentos> buscar(Lançamentos lancamentoFiltro);
	
	void atualizarStatus(Lançamentos lancamento, StatusLancamento status);
	
	void validar(Lançamentos lancamento);
}

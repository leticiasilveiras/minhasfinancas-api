package com.leticias.minhasfinancas.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.leticias.minhasfinancas.model.entity.Lançamentos;

public interface LancamentoRepository extends JpaRepository<Lançamentos, Long> {

}

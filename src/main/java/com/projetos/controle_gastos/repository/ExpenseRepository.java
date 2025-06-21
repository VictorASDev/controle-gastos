package com.projetos.controle_gastos.repository;

import com.projetos.controle_gastos.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findAllByUserId(UUID userId);
}

package com.projetos.controle_gastos.controller;

import com.projetos.controle_gastos.DTO.CreateExpenseDTO;
import com.projetos.controle_gastos.model.Expense;
import com.projetos.controle_gastos.repository.ExpenseRepository;
import com.projetos.controle_gastos.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
public class ExpenseController {
    private final UserRepository userRepository;
    private final ExpenseRepository expenseRepository;

    public ExpenseController(UserRepository userRespository, ExpenseRepository expenseRepository) {
        this.userRepository = userRespository;
        this.expenseRepository = expenseRepository;
    }

    @PostMapping("/expenses")
    public ResponseEntity<Void> createExpenses(@RequestBody CreateExpenseDTO expenseDTO,
                                               JwtAuthenticationToken token) {

        System.out.println("token:" + token.getName());
        var user = userRepository.findById(UUID.fromString(token.getName()))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        var expense = new Expense();
        expense.setUser(user);
        expense.setCust(expenseDTO.cust());
        expense.setName(expenseDTO.name());
        expense.setDate(Instant.now());

        expenseRepository.save(expense);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/expenses")
    public ResponseEntity<List<Expense>> listExpenses(JwtAuthenticationToken token) {
        var user = userRepository.findById(UUID.fromString(token.getName()))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        var expensesList = expenseRepository.findAllByUserId(user.getId());

        return ResponseEntity.ok(expensesList);
    }

    @DeleteMapping("/expenses/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable("id") long id, JwtAuthenticationToken token) {

        var user = userRepository.findById(UUID.fromString(token.getName()))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        var expense = expenseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Expense not found"));

        if(user.getId().equals(expense.getUser().getId())) {
            expenseRepository.deleteById(id);
        }
        else {
            throw new BadCredentialsException("Credentials don´t match");
        }
        return ResponseEntity.ok().build();
    }

}

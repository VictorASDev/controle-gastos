package com.projetos.controle_gastos.repository;

import com.projetos.controle_gastos.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}

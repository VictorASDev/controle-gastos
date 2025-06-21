package com.projetos.controle_gastos.controller;

import com.projetos.controle_gastos.DTO.UserRequest;
import com.projetos.controle_gastos.model.Role;
import com.projetos.controle_gastos.model.User;
import com.projetos.controle_gastos.repository.RoleRepository;
import com.projetos.controle_gastos.repository.UserRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@RestController
public class UserController {
    private final UserRepository userRespository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserController(UserRepository userRespository, BCryptPasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRespository = userRespository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @PostMapping("users")
    public ResponseEntity<Void> signIn(@RequestBody UserRequest userRequest) throws BadRequestException {
        if (userRespository.findByUsername(userRequest.username()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        User user = new User();
        user.setUsername(userRequest.username());
        user.setPassword(passwordEncoder.encode(userRequest.password()));
        user.setRoles(Set.of(roleRepository
                .findByName(Role.Values.USER.name())));
        userRespository.save(user);
        return ResponseEntity.ok().build();
    }
}

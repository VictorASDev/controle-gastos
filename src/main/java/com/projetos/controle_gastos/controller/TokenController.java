package com.projetos.controle_gastos.controller;

import com.projetos.controle_gastos.DTO.LoginRequest;
import com.projetos.controle_gastos.DTO.LoginResponse;
import com.projetos.controle_gastos.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.stream.Collectors;

@RestController
public class TokenController {
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;
    private final UserRepository userRespository;

    public TokenController(BCryptPasswordEncoder passwordEncoder,
                           JwtEncoder jwtEncoder, UserRepository userRespository) {
        this.passwordEncoder = passwordEncoder;
        this.jwtEncoder = jwtEncoder;
        this.userRespository = userRespository;
    }

    @CrossOrigin("*")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        var user = userRespository.findByUsername(loginRequest.username());
        if(user.isEmpty() || !passwordEncoder.matches(loginRequest.password(), user.get().getPassword())) {
            throw new BadCredentialsException("user or password invalid!");
        }

        var now = Instant.now();
        var expires = 300L;
        var roles = user.get().getRoles().stream()
                .map(role -> role.getName()).collect(Collectors.joining(" "));

        var claims = JwtClaimsSet.builder()
                .issuer("controle-gastos")
                .subject(user.get().getId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expires))
                .claim("scope", roles)
                .build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return ResponseEntity.ok(new LoginResponse(jwtValue, expires));
    }
}

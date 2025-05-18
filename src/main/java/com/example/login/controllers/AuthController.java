package com.example.login.controllers;

import com.example.login.domain.user.User;
import com.example.login.dto.LoginRequestDTO;
import com.example.login.dto.RegisterRequestDTO;
import com.example.login.dto.ResponseDTO;
import com.example.login.infra.security.TokenService;
import com.example.login.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository repository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequestDTO body) {
        User user = this.repository.findByEmail(body.email()).orElseThrow(() -> new RuntimeException("Email not found"));
        if (passwordEncoder.matches(user.getPassword(), body.password())) {
            String token = tokenService.generateToken(user);
            return ResponseEntity.ok(new ResponseDTO(user.getUsername(), token));
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterRequestDTO body) {

        Optional<User> user = this.repository.findByEmail(body.email());

        if (user.isEmpty()) {
            User newUser = new User();
            newUser.setPassword(passwordEncoder.encode(body.password()));
            newUser.setUsername(body.email());
            newUser.setUsername(body.name());
            this.repository.save(newUser);


                String token = tokenService.generateToken(newUser);
                return ResponseEntity.ok(new ResponseDTO(newUser.getUsername(), token));

        }
        return ResponseEntity.badRequest().build();
    }
}

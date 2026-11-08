package com.university.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.university.dto.AccountDTO;
import com.university.dto.CreateAccountRequest;
import com.university.entity.UserAccount;
import com.university.repository.UserAccountRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class AccountController {
    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountController(UserAccountRepository userAccountRepository, PasswordEncoder passwordEncoder) {
        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public ResponseEntity<AccountDTO> createAccount(@Valid @RequestBody CreateAccountRequest request) {
        UserAccount account = new UserAccount();
        account.setUsername(request.username());
        account.setPassword(passwordEncoder.encode(request.password()));
        account.setRole(request.role());
        UserAccount created = userAccountRepository.save(account);
        AccountDTO response = new AccountDTO(created.getUsername(), created.getRole());
        return ResponseEntity.created(URI.create("/api/users/" + created.getUsername())).body(response);
    }
}

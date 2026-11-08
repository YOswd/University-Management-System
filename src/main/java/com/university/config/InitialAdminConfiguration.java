package com.university.config;

import com.university.entity.UserAccount;
import com.university.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class InitialAdminConfiguration {
    @Bean
    ApplicationRunner createInitialAdmin(
            UserAccountRepository userAccountRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.security.initial-admin.username:admin}") String username,
            @Value("${app.security.initial-admin.password:}") String password) {
        return arguments -> {
            if (password.isBlank()) {
                throw new IllegalStateException(
                        "Set INITIAL_ADMIN_PASSWORD to a password of at least 12 characters before starting the application");
            }
            if (password.length() < 12) {
                throw new IllegalStateException("INITIAL_ADMIN_PASSWORD must contain at least 12 characters");
            }
            if (!username.matches("[A-Za-z0-9._-]{3,50}")) {
                throw new IllegalStateException("INITIAL_ADMIN_USERNAME must be 3 to 50 letters, numbers, '.', '_' or '-'");
            }
            UserAccount existingAccount = userAccountRepository.findByUsername(username).orElse(null);
            if (existingAccount != null) {
                if (!"ADMIN".equals(existingAccount.getRole())) {
                    throw new IllegalStateException("The configured initial admin username belongs to a non-admin account");
                }
                return;
            }
            UserAccount account = new UserAccount();
            account.setUsername(username);
            account.setPassword(passwordEncoder.encode(password));
            account.setRole("ADMIN");
            userAccountRepository.save(account);
        };
    }
}

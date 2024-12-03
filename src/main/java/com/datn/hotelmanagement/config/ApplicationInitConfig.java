package com.datn.hotelmanagement.config;


import com.datn.hotelmanagement.Repository.AccountRepo;
import com.datn.hotelmanagement.entity.Account;
import com.datn.hotelmanagement.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ApplicationInitConfig {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(AccountRepo accountRepo) {
        return args -> {
            if (accountRepo.findByUserName("admin").isEmpty()) {
                var role = Role.ADMIN.name();

                Account user = Account.builder()
                        .userName("Admin")
                        .password(passwordEncoder.encode("admin"))
                        .role(role)
                        .build();

                accountRepo.save(user);
            }
        };
    }
}
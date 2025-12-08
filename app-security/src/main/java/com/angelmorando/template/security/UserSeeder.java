package com.angelmorando.template.security;

import com.angelmorando.template.dao.UserAuthDao;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class UserSeeder {

    @Bean
    public CommandLineRunner seedUsers(UserAuthDao dao, PasswordEncoder encoder) {
        return args -> {
            // seed a basic user if not exists
            var existing = dao.selectUserByUsername("user");
            if (existing == null || existing.isEmpty()) {
                Map<String, Object> user = new HashMap<>();
                user.put("username", "user");
                user.put("password", encoder.encode("password"));
                user.put("enabled", true);
                user.put("accountNonExpired", true);
                user.put("accountNonLocked", true);
                user.put("credentialsNonExpired", true);
                dao.insertUser(user);
                dao.insertAuthority("user", "ROLE_USER");
            }
        };
    }
}

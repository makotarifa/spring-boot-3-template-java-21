package com.angelmorando.template.security.auth; 

import com.angelmorando.template.persistence.auth.dao.UserAuthDao;
import com.angelmorando.template.persistence.auth.model.UserRow;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@ConditionalOnProperty(name = "app.security.enabled", havingValue = "true", matchIfMissing = false)
public class UserSeeder {

    @Bean
    public CommandLineRunner seedUsers(UserAuthDao dao, PasswordEncoder encoder) {
        return args -> {
            // seed a basic user if not exists
            var existing = dao.selectUserByUsername("user");
            if (existing == null) {
                UserRow user = new UserRow();
                user.setUsername("user");
                user.setPassword(encoder.encode("password"));
                user.setEnabled(true);
                user.setAccountNonExpired(true);
                user.setAccountNonLocked(true);
                user.setCredentialsNonExpired(true);
                dao.insertUser(user);
                dao.insertAuthority("user", "ROLE_USER");
            }
        };
    }
}

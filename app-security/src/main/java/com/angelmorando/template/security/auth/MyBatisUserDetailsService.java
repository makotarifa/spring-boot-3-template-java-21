package com.angelmorando.template.security.auth; 

import com.angelmorando.template.persistence.auth.dao.UserAuthDao;
import com.angelmorando.template.persistence.auth.model.UserRow;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MyBatisUserDetailsService implements UserDetailsService {

    private final UserAuthDao dao;

    public MyBatisUserDetailsService(UserAuthDao dao) {
        this.dao = dao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserRow u = dao.selectUserByUsername(username);
        if (u == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        List<String> auths = dao.selectAuthoritiesByUsername(username);
        var authorities = auths.stream()
                .map(org.springframework.security.core.authority.SimpleGrantedAuthority::new)
                .toList();

        boolean enabled = u.getEnabled() == null || u.getEnabled();

        return User.withUsername(u.getUsername())
                .password(u.getPassword())
                .authorities(authorities)
                .disabled(!enabled)
                .build();
    }
}

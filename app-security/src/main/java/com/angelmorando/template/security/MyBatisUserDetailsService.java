package com.angelmorando.template.security.auth; 

import com.angelmorando.template.persistence.auth.dao.UserAuthDao;
import com.angelmorando.template.persistence.auth.model.UserRow;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
                .map(a -> new org.springframework.security.core.authority.SimpleGrantedAuthority(a))
                .collect(Collectors.toList());

        boolean enabled = u.getEnabled() == null ? true : u.getEnabled();

        return User.withUsername(u.getUsername())
                .password(u.getPassword())
                .authorities(authorities)
                .disabled(!enabled)
                .build();
    }
}

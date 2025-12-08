package com.angelmorando.template.security;

import com.angelmorando.template.dao.UserAuthDao;
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
        Map<String, Object> u = dao.selectUserByUsername(username);
        if (u == null || u.isEmpty()) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        List<String> auths = dao.selectAuthoritiesByUsername(username);
        var authorities = auths.stream()
                .map(a -> new org.springframework.security.core.authority.SimpleGrantedAuthority(a))
                .collect(Collectors.toList());

        boolean enabled = u.get("enabled") == null ? true : Boolean.parseBoolean(String.valueOf(u.get("enabled")));

        return User.withUsername((String) u.get("username"))
                .password((String) u.get("password"))
                .authorities(authorities)
                .disabled(!enabled)
                .build();
    }
}

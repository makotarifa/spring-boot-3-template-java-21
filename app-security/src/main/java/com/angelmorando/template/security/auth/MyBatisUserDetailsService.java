package com.angelmorando.template.security.auth; 

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.angelmorando.template.persistence.auth.dao.UserAuthDao;
import com.angelmorando.template.persistence.auth.model.UserRow;
import com.angelmorando.template.mappers.auth.UserMapper;

@Service
public class MyBatisUserDetailsService implements UserDetailsService {

    private final UserAuthDao dao;
    private final UserMapper userMapper;

    public MyBatisUserDetailsService(UserAuthDao dao, UserMapper mapper) {
        this.dao = dao;
        this.userMapper = mapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserRow uRow = dao.selectUserByUsername(username);
        var u = userMapper.toDomain(uRow);
        if (u == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        List<String> auths = dao.selectAuthoritiesByUsername(username);
        var authorities = auths.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();

        boolean enabled = u.getEnabled() == null || u.getEnabled();

        return User.withUsername(u.getUsername())
            .password(u.getPassword())
                .authorities(authorities)
                .disabled(!enabled)
                .build();
    }
}

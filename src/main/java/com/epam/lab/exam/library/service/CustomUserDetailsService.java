package com.epam.lab.exam.library.service;

import com.epam.lab.exam.library.model.User;
import com.epam.lab.exam.library.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(userName)
                .orElseThrow(() -> new UsernameNotFoundException("Login " + userName + " not found"));
        UserDetails build = org.springframework.security.core.userdetails.User.builder().username(user.getLogin())
                .password(user.getChecksum()).authorities(getAuthorities(user))
                .build();
        return build;
    }

    private static Collection<? extends GrantedAuthority> getAuthorities(User user) {
        List<String> userRoles = new ArrayList<>(
                Arrays.asList(user.getRole().getType().toString(), "ROLE_".concat(user.getRole().getType().toString())));
        Collection<GrantedAuthority> authorities = AuthorityUtils
                .createAuthorityList(userRoles.stream().toArray(String[]::new));
        return authorities;
    }
}
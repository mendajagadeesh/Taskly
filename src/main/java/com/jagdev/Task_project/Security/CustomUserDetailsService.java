package com.jagdev.Task_project.Security;

import com.jagdev.Task_project.entity.Users;
import com.jagdev.Task_project.exception.UserNotFound;
import com.jagdev.Task_project.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService
{
    @Autowired
    private UsersRepository usersRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users users=usersRepository.findByEmail(email).orElseThrow(
                () -> new UserNotFound(String.format("User with email %s is not found",email))
        );
        Set<String> roles=new HashSet<>();
        roles.add("ROLE_ADMIN");
        roles.add("ROLE_USER");
        return User.builder()
                .username(users.getEmail())
                .password(users.getPassword())
                .authorities(userAuthorities(roles))
                .build();
    }

    private Collection<? extends GrantedAuthority> userAuthorities(Set<String> roles) {
        return roles.stream().map(
                role -> new SimpleGrantedAuthority(role)
        ).collect(Collectors.toList());
    }
}

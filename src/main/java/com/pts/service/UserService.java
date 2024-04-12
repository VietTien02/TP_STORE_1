package com.pts.service;

import com.pts.dao.AccountDAO;
import com.pts.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    AccountDAO accountDAO;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            Account account = accountDAO.laytk(username);
            if (!account.getTps_Active()) {
                throw new UsernameNotFoundException(username + " is not active!");
            }
            String storedPassword = account.getTps_Password();
            return User.withUsername(username)
                    .password(storedPassword)
                    .roles(account.getAuthorities().stream()
                            .map(au -> au.getRole().getTps_Id())
                            .collect(Collectors.toList())
                            .toArray(new String[0]))
                    .build();
        } catch (Exception e) {
            throw new UsernameNotFoundException(username + " not found!");
        }
    }
}





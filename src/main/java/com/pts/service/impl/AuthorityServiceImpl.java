package com.pts.service.impl;

import com.pts.dao.AccountDAO;
import com.pts.dao.AuthorityDao;
import com.pts.service.AuthorityService;
import com.pts.entity.Account;
import com.pts.entity.Authority;
import com.pts.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorityServiceImpl implements AuthorityService {
    @Autowired
    AuthorityDao authDao;
    @Autowired
    AccountDAO daoA;
    @Override
    public Authority save(Account account) {
        Authority auth = new Authority();
        Role role = new Role();
        auth.setAccount(account);
        role.setTps_Id("User");
        auth.setRole(role);
        return authDao.save(auth);
    }



}


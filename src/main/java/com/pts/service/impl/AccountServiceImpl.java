package com.pts.service.impl;

import com.pts.dao.AccountDAO;
import com.pts.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    AccountDAO adao;



}

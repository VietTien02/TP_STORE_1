package com.pts.service.impl;

import com.pts.dao.AccountDAO;
import com.pts.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceIml {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    AccountDAO accountDAO;

    public Account save(Account user) {
//         Mã hóa mật khẩu trước khi lưu vào database
//        user.setTps_Password(hashPassword(user.getTps_Password()));
//        String encodedPassword = bCryptPasswordEncoder.encode(user.getTps_Password());
//        user.setTps_Password(encodedPassword);
        return accountDAO.save(user);
    }

    public String hashPassword(String password) {
        // Sử dụng BCrypt để mã hóa mật khẩu
        return bCryptPasswordEncoder.encode(password);
    }
}

package com.pts.controller.site;

import com.pts.dao.AccountDAO;
import com.pts.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ChangePasswordController {
    @Autowired
    AccountDAO accountDAO;
    @RequestMapping("/change-password")
    public String changepassword(){
        return "site/doimatKhau";
    }
    @PostMapping("/doi-mat-khau")
    public String doimatkhauh(Model m,@RequestParam("taikhoan") String taikhoan
                                ,@RequestParam("password")String matkhaucu,
                              @RequestParam("passwordmoi")String matkhaumoi
                              ){
        Account acc = accountDAO.laytk(taikhoan);
        String matkhauht =acc.getTps_Password();

        if(matkhauht.equals(matkhaucu)){
            acc.setTps_Password(matkhaumoi);
            accountDAO.save(acc);
            m.addAttribute("tb","Bạn đã thay đổi mật khẩu thành công");
            return "site/doimatKhau";
        }else {
            m.addAttribute("tb","Mật khẩu hiện tại không chính xác");
            return "site/doimatKhau";
        }


    }


}

package com.pts.controller.site;

import com.pts.dao.AccountDAO;
import com.pts.service.impl.MailerServiceImpl;
import com.pts.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.MessagingException;

@Controller
public class QuenMKController {
    @Autowired
    AccountDAO accountDAO;

    @Autowired
    MailerServiceImpl mailer;

    @GetMapping("/quenpass")
    public String quenMk1(Model m){
        return "site/quenmatkhau";

    }
    @PostMapping("quen-mat-khau")
    public String quenmkpost(Model m,@RequestParam("email")String email) throws MessagingException {
        Account acc=accountDAO.laytk(email);
        if(acc!=null){
            if(!acc.getTps_Active()){
                m.addAttribute("error","Tài Khoản không tồn tại");
                return "site/quenmatkhau";
            }else{

                acc.generateResetToken();
                accountDAO.save(acc);
                String token=acc.getResetToken();
                String activationUrl = "http://localhost:8080/quenmk?token=" +token;

                mailer.send( acc.getTps_Username(),"<h1>Quên Mật Khẩu</h1>","Xin Chào  Chúng tôi đã nhận được yêu cầu Quên Mật Khẩu vào F5 bằng địa chỉ email này. Nếu bạn muốn Thay Đổi Mật Khẩu bằng tài khoản "+acc.getTps_Username()+" của mình, hãy nhấp vào liên kết:"+"<a href='" + activationUrl + "'>tại đây</a>");

                m.addAttribute("error","Quên Mật Khẩu Thành Công, Vui lòng kiểm tra email");
                return "site/quenmatkhau";
            }
        }
        return "site/quenmatkhau";

    }


    @RequestMapping("quenmk")
    public String quenmkac(Model m, @RequestParam("token") String token){

        m.addAttribute("token", token);
        return "site/quenmk";
    }
    @PostMapping("/thay-doi-mat-khau")
    public String quenmk(Model m, @RequestParam("token") String token, @RequestParam("password") String password) {
        Account acc = accountDAO.laytokenreset(token);
        if (acc == null) {
            String script = "<script>alert('Mã Token Đã Hết Hạn');</script>";
            m.addAttribute("script", script);
            return "site/quenmk";
        } else {
            acc.setTps_Password(password);
            acc.setResetToken(null);
            accountDAO.save(acc);
            String script = "<script>alert('mật khẩu đã được thay đổi');</script>";
            m.addAttribute("script", script);
            return "site/quenmk";
        }
    }





}

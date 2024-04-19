package com.pts.controller.site;

import com.pts.dao.AccountDAO;
import com.pts.service.AuthorityService;
import com.pts.service.impl.MailerServiceImpl;
import com.pts.service.impl.UserServiceIml;
import com.pts.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.MessagingException;
import java.util.Date;

@Controller
public class dangKyController {

    @Autowired
    AccountDAO accountDAO;
//    @Autowired
//    private UserService userService;

    @Autowired
    AuthorityService authorityService;

    @Autowired
    MailerServiceImpl mailer;
    @Autowired
    UserServiceIml userServiceIml;
//    @Autowired
//    BCryptPasswordEncoder passwordEncoder;

    @RequestMapping("auth/signup")
    public String signup(Model m, Account account){
//        m.addAttribute("acc",account);
        return "site/dangky";
    }
    @PostMapping("/dang-ky")
    public  String dangky(Model m,
                          @RequestParam("tps_Password") String password,
                          @RequestParam("tps_Gmail") String username,@RequestParam("fullname")String fullname) throws MessagingException {
        // kiem tra tai khoan đã sử dụng chưa
        Account acc = accountDAO.laytk(username);
        if (  acc != null) {
            if (acc.getTps_Active()) {
                // tai khoan da active
                m.addAttribute("error", "Tên đăng nhập đã được sử dụng");

                return "/site/dangky";
            } else {
                // tai khoan chua active
                acc.setTps_Password(password);
                acc.setTps_Fullname(fullname);
                accountDAO.save(acc);
                String activationUrl = "http://localhost:80/activate?token=" + acc.getActivationToken();
                m.addAttribute("error", "Tài Khoản đang chờ kích hoạt Vui lòng kiểm tra gmail!");
                mailer.send(username,"Kích Hoạt Tài Khoản","Xin Chào  Chúng tôi đã nhận được yêu cầu đăng nhập vào F5 bằng địa chỉ email này. Nếu bạn muốn đăng nhập bằng tài khoản "+acc.getTps_Username()+" của mình, hãy nhấp vào liên kết:"+"<a href='" + activationUrl + "'>tại đây</a>");
                return "site/dangky";
            }
        }

        // tao moi tai khoan
        acc = new Account();
        acc.setTps_Fullname(fullname);
        acc.setTps_Username(username);
//        acc.setTps_Password(passwordEncoder.encode(password));
        acc.setTps_Password(password);
        acc.setTps_Active(false);
        acc.setTps_Date(new Date());
        acc.setTps_Photo("https://res.cloudinary.com/djfbh9dqg/image/upload/v1678788065/photo/logo_oqagna.jpg");
        acc.generateActivationToken();
        userServiceIml.save(acc);
        authorityService.save(acc);
        String activationUrl = "http://localhost:80/activate?token=" + acc.getActivationToken();
        m.addAttribute("error", "Tài Khoản đang chờ kích hoạt Vui lòng kiểm tra gmail!");
        mailer.send(username,"Kích Hoạt Tài Khoản","Xin Chào  Chúng tôi đã nhận được yêu cầu đăng nhập vào F5 địa chỉ email này. Nếu bạn muốn đăng nhập bằng tài khoản "+acc.getTps_Username()+" của mình, hãy nhấp vào liên kết:"+"<a href='" + activationUrl + "'>tại đây</a>");

        return "site/dangky";
    }

    @GetMapping("/activate")
    public String activate(Model m, @RequestParam("token") String token) {
        Account acc = accountDAO.findByActivationToken(token);
        if (acc != null) {
            acc.setTps_Active(true);
            acc.setActivationToken(null); // clear the activation token
            accountDAO.save(acc);
            m.addAttribute("error","Kích Hoạt Tài Khoản Thành Công");
            return "site/dangnhap";
        } else {
            m.addAttribute("error", "Mã Token kích hoạt không hợp lệ");
            return "site/dangky";
        }
    }

}

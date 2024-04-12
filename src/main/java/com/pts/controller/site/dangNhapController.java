package com.pts.controller.site;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class dangNhapController {
    @RequestMapping("/auth/signin")
    public String dangnhap(Model m){
        return "/site/dangnhap";
    }
    @RequestMapping("/login/thanhcong")
    public String logintc(Model m){
        m.addAttribute("message","đăng nhập thành công");
        return "/site/dangnhap";
    }

    @RequestMapping("/login/error")
    public String loginer(Model m){
            m.addAttribute("message","Sai thông tin Đăng Nhập");
        return "/site/dangnhap";
    }
    @RequestMapping("/login/unauthoried")
    public String unauthoried(Model m){
        m.addAttribute("message","Không Có Quyền Truy Xuất");
        return "/site/dangnhap";
    }
    @RequestMapping("/looff/success")
    public String looff(Model m){
        m.addAttribute("message","Bạn Đã Đăng Xuất");
        return "/site/dangnhap";
    }
}

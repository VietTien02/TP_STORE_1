package com.pts.help;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorThanhToanController {
    @RequestMapping("/error-thanh-toan")
    public String error(){
        return "/site/errorThanhToan";
    }
}

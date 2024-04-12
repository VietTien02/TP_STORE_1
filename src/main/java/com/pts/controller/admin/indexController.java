package com.pts.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class indexController {

    @RequestMapping("admin")
    public String qltk(Model m){

        return "Admin/index";
    }
}

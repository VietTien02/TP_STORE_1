package com.pts.controller.site;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class erorController {

    @GetMapping("/error")
    public String error(){
        return "site/error";
    }

    @GetMapping("thanh-toan-thanh-cong")
    public String thanhtoantc(@RequestParam("courseId") int courseId, Model m,@RequestParam("ContentId") int ContentId){
        m.addAttribute("ID", courseId);
        m.addAttribute("ContentId", ContentId);

        return "/site/CourseOK";
    }

    @GetMapping("/error-quiz")
    public String errorquiz(){
        return "site/help/errorQuiz";
    }
    @GetMapping("/succes-quiz")
    public String SuccesQuiz(){
        return "site/help/Succes";
    }
    @GetMapping("/next-quiz")
    public String nextQuiz(){
        return "site/help/nextQuiz";
    }


}

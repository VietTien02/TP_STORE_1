package com.pts.controller.site;

import com.pts.dao.CourseDAO;
import com.pts.entity.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class DanhMucController {
    @Autowired
    CourseDAO courseDAO;

    @RequestMapping("/danh-muc/{id}")
    public String danhmuc(@PathVariable("id") int id, Model m){
        List<Course> courses =courseDAO.laydanhmuc(id);
        m.addAttribute("danhmuc",courses);
        m.addAttribute("categoryName", courses.get(0).getCategory().getTps_Name());
        return "/site/danhmuc";
    }
}

package com.pts.controller.admin;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pts.dao.CourseDAO;
import com.pts.dao.OrderDAO;

import java.util.List;

@Controller
@RequestMapping("/admin/CourseSales")
public class CourseSalesController {
	@Autowired
    private CourseDAO courseDAO;

    @Autowired
    private OrderDAO orderDAO;

    @GetMapping
    public String showAllCourses(Model model) {
        List<Object[]> courses = courseDAO.findAllCoursesAndSoldCount();
        model.addAttribute("courses", courses);

        return "/Admin/CourseSale";
    }


}

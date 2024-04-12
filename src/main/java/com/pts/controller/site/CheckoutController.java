package com.pts.controller.site;

import com.pts.dao.AccountDAO;
import com.pts.dao.CourseDAO;
import com.pts.dao.OrderDAO;
import com.pts.dao.UserContentDAO;
import com.pts.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;


@Controller
public class CheckoutController {
    @Autowired
    CourseDAO courseDAO;
    @Autowired
    OrderDAO orderDAO;
    @Autowired
    AccountDAO accountDAO;
    
    @Autowired
    UserContentDAO userContentDAO;
    @RequestMapping("/checkout/{id}")
    private String index(Model m, @PathVariable("id")int id){
        Course course =courseDAO.findById(id);
        m.addAttribute("checkout",course);
        return "/site/checkout";

    }
    @GetMapping("/add-course/{id}")
    public String themkhoahoc(Model m, @PathVariable("id")int id, HttpServletRequest request){
       
        Course course=courseDAO.findById(id);
        
        String username=request.getRemoteUser();
        Account acc =accountDAO.laytk(username);
        int gia =course.getTps_Price();
        if(gia !=0) {
            m.addAttribute("error","Bạn Không Có Quyền Hạn Để Thêm Khóa Học");
        	return "site/error";
        }else {
        	Order order = new Order();
            order.setTps_username(username);
            order.setCourseor(course);
            order.setTps_statuscode(false);
            order.setPay("Khóa Học Miễn Phí");
            orderDAO.save(order);
            UserContent userContent = new UserContent();
            Content firstContent = null;
            for (Chapter chapter : course.getChapters()) {
                for (Content content : chapter.getContents()) {
                    if (content.getTps_linkytb() != null) {
                        firstContent = content;
                        break;
                    }
                }
                if (firstContent != null) {
                    break;
                }
            }
            if (firstContent != null) {
                userContent.setAccount(acc);
                userContent.setCourse(course);
                userContent.setCompleted(true);
                userContent.setContent(firstContent);
                userContent.setTime("0:0:0");
                userContentDAO.save(userContent);

                return "redirect:/learning/" +firstContent.getTps_id()+ "/" + id;

            }


            return "redirect:/";

        }

    }
}

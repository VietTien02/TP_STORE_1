package com.pts.controller.site;

import com.pts.dao.AccountDAO;
import com.pts.dao.UserContentDAO;
import com.pts.service.OrderService;

import com.pts.entity.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

@Controller
public class myCrouseController {
    @Autowired
    AccountDAO accountDAO;
    @Autowired
    UserContentDAO userContentDAO;

    @Autowired
    OrderService orderService;
    @RequestMapping("/my-courses")
    public String index(Model m, HttpServletRequest request) {
        String username = request.getRemoteUser();
        List<Order> mycrouse = orderService.myCrouse(username);


        List<Double> courseProgresses = new ArrayList<>();
        for (Order order : mycrouse) {
            Course course = order.getCourseor();
            // Đếm tổng số nội dung trong khóa học
            int totalContents = 0;
            for (Chapter chapter : course.getChapters()) {
                totalContents += chapter.getContents().size();
            }
            // Đếm số nội dung đã hoàn thành trong khóa học
            int completedContents = 0;
            List<UserContent> userContents = userContentDAO.layvideodanghoc(username, course.getTps_id());
            for (UserContent userContent : userContents) {
                if (userContent.isCompleted()) {
                    completedContents++;
                }
            }
            // Tính tiến độ khóa học
            double courseProgress = 0.0;
            if (totalContents > 0) {
                courseProgress = ((double) completedContents / (double) totalContents) * 100.0;
            }
            courseProgresses.add(courseProgress);
        }

        List<Integer> maxContentIds = new ArrayList<>();
        for (Order order : mycrouse) {
            List<UserContent> contentIds = userContentDAO.layvideodanghoc(username, order.getCourseor().getTps_id());
            int maxContentId = 0;
            for (UserContent userContent : contentIds) {
                if (userContent.isCompleted() && userContent.getContent().getTps_id() > maxContentId) {
                    maxContentId = userContent.getContent().getTps_id();
                }
            }
            maxContentIds.add(maxContentId);
        }

        m.addAttribute("maxContentIds", maxContentIds);
        m.addAttribute("mycrouse", mycrouse);
        m.addAttribute("courseProgresses", courseProgresses);

        return "site/myCrouse";
    }








}

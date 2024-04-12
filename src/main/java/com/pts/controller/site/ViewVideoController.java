package com.pts.controller.site;


import com.pts.dao.*;
import com.pts.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ViewVideoController {
    @Autowired
    CourseDAO courseDAO;
    @Autowired
    ChapterDAO chapterDAO;
    @Autowired
    ContentDAO contentDAO;

    @Autowired
    OrderDAO orderDAO;
    @Autowired
    UserContentDAO userContentDAO;
    @Autowired
    AccountDAO accountDAO;
    @Autowired
    QuizQuestionDAO quizQuestionDAO;
    @Autowired
    QuizResultDAO quizResultDAO;

    @RequestMapping("/learning/{content}/{course}")
    public String ct(Model m, @PathVariable("content") int contentId, @PathVariable("course") int courseId, HttpServletRequest request) {
        String username = request.getRemoteUser();
        Order or = orderDAO.kiemtraCrouse(courseId, username);
        UserContent userContent = userContentDAO.addtime(username, contentId);
        if (userContent == null) {
            m.addAttribute("error", "Bạn Chưa Hoàn Thành Khóa Học");
            return "site/error";
        }
        String time = userContent.getTime();

        m.addAttribute("userContent",userContent);
        if (or == null) {
            m.addAttribute("error", "Bạn Chưa Mua Khóa Học");
            return "site/error";
        }   else if (time.equalsIgnoreCase("0:0:0") || time.equals("Complete")) {
            m.addAttribute("owned", false);

        } else {
            String[] tokens = time.split(":");
            int hours = Integer.parseInt(tokens[0]);
            int minutes = Integer.parseInt(tokens[1]);
            int seconds = Integer.parseInt(tokens[2]);
            int times=hours * 3600 + minutes * 60 + seconds;
            m.addAttribute("owned", true);
            m.addAttribute("time",times);


        }
        List<QuizResult>quizResults =quizResultDAO.ketqua(username,contentId,courseId);
        if (quizResults.isEmpty()) {
//            System.out.println("Không có kết quả");
        } else {
            int totalScore = 0;
            for (QuizResult quizResult : quizResults) {
                totalScore += quizResult.getScore();
            }
            m.addAttribute("totalScore", totalScore);
        }
        m.addAttribute("quizResults",quizResults);




        Course course = courseDAO.findById(courseId);
        Content contents = contentDAO.connn(contentId);

        List<QuizQuestion> quizQuestions = quizQuestionDAO.findQuizQuestion(contentId);


        m.addAttribute("listQuiz", quizQuestions);
        m.addAttribute("radioCount", 0);
        String name = course.getTps_Name();
        String firstLink = null;
        String title = null;
        List<UserContent> userContents = userContentDAO.kiemtracontent(username);
        m.addAttribute("userContents", userContents);

        for (Chapter chapter : course.getChapters()) {
            for (Content content : chapter.getContents()) {
                if (content.getTps_linkytb() != null) {
                    firstLink = contents.getTps_linkytb();
                    title = contents.getTps_title();
                    break;
                }
            }
            if (firstLink != null) {
                break;
            }
        }
        m.addAttribute("contend", contentId);
        m.addAttribute("ListMycourse", course);
        m.addAttribute("firstLink", firstLink);
        m.addAttribute("name", name);
        m.addAttribute("title", title);
        m.addAttribute("courseId", courseId);

        return "site/viewvideo";

    }
    }

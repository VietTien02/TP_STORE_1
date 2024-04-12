package com.pts.controller.admin;

import com.pts.dao.ChapterDAO;
import com.pts.dao.ContentDAO;
import com.pts.dao.CourseDAO;
import com.pts.dao.QuizDAO;
import com.pts.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/quiz")
public class QuizControllerAdmin {
    private final int pageSize = 8;
    @Autowired
    CourseDAO courseDAO;

    @Autowired
    ChapterDAO chapterDAO;
    @Autowired
    ContentDAO contentDAO;

    @Autowired
    QuizDAO quizDAO;


    @GetMapping
    public String index(Model m,@RequestParam(defaultValue = "1") int page,
                        @RequestParam(defaultValue = "") String keyword,
                        @RequestParam(defaultValue = "") String courseId,
                        @RequestParam(defaultValue = "") String chapterId){
        List<Course> course = courseDAO.findAll();
        m.addAttribute("course",course);

        List<Quiz> quizList;
        if (keyword == "") {
            quizList = quizDAO.findAll().stream().filter(q ->q.getStatus()).collect(Collectors.toList());
            m.addAttribute("course", quizList);

        } else {
            quizList = quizDAO.timkiemKhoaHoc(keyword).stream().filter(q ->q.getStatus()).collect(Collectors.toList());
        }

        int totalAccounts = quizList.size();
        int totalPages = (int) Math.ceil(totalAccounts / (double) pageSize);

        // Lấy danh sách tài khoản trên trang hiện tại
        int start = (page - 1) * pageSize;

        int end = Math.min(start + pageSize, totalAccounts);
        List<Quiz> accountsOnPage = quizList.subList(start, end);

        // Đưa thông tin về dữ liệu và phân trang vào Model
        AccountPage accountPage = new AccountPage();
        accountPage.setQuizList(accountsOnPage);
        accountPage.setTotalPages(totalPages);
        accountPage.setCurrentPage(page);
        m.addAttribute("accountPage", accountPage);
        if(courseId ==null || courseId.equals("")){

        }else {
            int idCourse=Integer.parseInt(courseId);
            List<Chapter>chapters =chapterDAO.findByCourse(idCourse);
            m.addAttribute("chapters",chapters);
            Course cour =courseDAO.findById(idCourse);
            m.addAttribute("cour",cour);
        }
        if(chapterId ==null ||chapterId.equals("")){
        }else{

            int idChapter=Integer.parseInt(chapterId);
            List<Content>contents=contentDAO.timchapter(idChapter);
            m.addAttribute("contents",contents);
            Chapter chap =chapterDAO.findcourse(idChapter);
            m.addAttribute("chap",chap);

        }


        return "Admin/BaiQuiz";

    }



}

package com.pts.controller.site;

import com.pts.dao.*;
import com.pts.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
public class QuizController {
    @Autowired
    QuizQuestionDAO quizQuestionDAO;
    @Autowired
    QuizDAO quizDAO;

    @Autowired
    AccountDAO accountDAO;

    @Autowired
    QuizResultDAO quizResultDAO;
    @Autowired
    ContentDAO contentDAO;
    @Autowired
    UserContentDAO UsercontentDAO;

    @PostMapping("/quiz")
    public String submitQuiz(HttpServletRequest request, @RequestParam("id") int id,
                             Model model,
                             @RequestParam Map<String, String> formData,
                             @RequestParam("courseId")int courseId) {
        // Lấy danh sách câu hỏi và đáp án đúng của bài quiz

        Quiz quiz = quizDAO.layquiz(id);
        List<QuizQuestion> quizQuestions = quiz.getQuizQuestionList();
        String user = request.getRemoteUser();
        Account acc = accountDAO.laytk(user);
        int score = 0;
        // Kiểm tra xem người dùng đã làm bài quiz này trước đó chưa
        List<QuizResult> existingResult = quizResultDAO.ktbai(acc.getTps_Username(), quiz.getId());

        if (existingResult != null) {
            // Nếu đã làm bài quiz trước đó, xóa tất cả các bản ghi liên quan đến người dùng và bài quiz đó
            quizResultDAO.deleteAll(existingResult);
        }

        for (int i = 0; i < quizQuestions.size(); i++) {
            QuizQuestion question = quizQuestions.get(i);
            String selectedOption = formData.get("checkbox-group-" + i).trim();
            boolean isCorrect = selectedOption != null && selectedOption.equals(question.getCorrectoption());
            int questionScore = isCorrect ? 1 : 0;

            QuizResult quizResult = new QuizResult();
            quizResult.setAccount(acc);
            quizResult.setQuiz(quiz);
            quizResult.setQuestion(question);
            quizResult.setUser_answer(selectedOption);
            quizResult.setCorrect(isCorrect);
            quizResult.setScore(questionScore);
            quizResultDAO.save(quizResult);
            score += questionScore;
        }
        if(score <5){
            model.addAttribute("error","Bạn Chưa Hoàn Thành Bài Quiz. Vui Lòng Học  Lại Diểm Của Bạn là: " +score);
            model.addAttribute("courseId",courseId);
                model.addAttribute("contend",id);

            return "site/help/errorQuiz";

        }else {
            // Mở khóa khóa học tiếp theo
            // Lấy id của content hiện tại
            int currentContentId = id;
            // Lấy id của content tiếp theo
            List<Content> nextContentList = contentDAO.getNextContent(courseId, currentContentId, PageRequest.of(0, 1));

            if (nextContentList != null && !nextContentList.isEmpty()) {
                Content nextContent = nextContentList.get(0);

                UserContent content =UsercontentDAO.findNextContent(user,nextContent.getTps_id(),courseId);
                if(content != null){
                    model.addAttribute("score",score);
                    model.addAttribute("nextContent",nextContent.getTps_id());
                    model.addAttribute("course",courseId);
                    return "site/help/nextQuiz";
                }
                UserContent userContent = new UserContent();
                userContent.setAccount(acc);
                userContent.setContent(nextContent);
                userContent.setCompleted(true);
                userContent.setCourse(nextContent.getChapter().getCourse());
                userContent.setTime("0:0:0");
                UsercontentDAO.save(userContent);

                model.addAttribute("score",score);
                model.addAttribute("nextContent",userContent.getContent().getTps_id());
                model.addAttribute("course",courseId);
                return "site/help/nextQuiz";
            }else {


                return "site/help/Succes";
            }
        }


            }



    }

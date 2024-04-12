package com.pts.controller.admin;

import com.pts.dao.QuizDAO;
import com.pts.dao.QuizQuestionDAO;
import com.pts.entity.Quiz;
import com.pts.entity.QuizQuestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/quiz-add")
public class QuizQuestionController {
    @Autowired
    QuizDAO quizDAO;

    @Autowired
    QuizQuestionDAO quizQuestionDAO;
    @GetMapping
    public String index(Model m,Quiz quiz){
        List<Quiz> quizList = quizDAO.findAll()
                .stream()
                .filter(q -> !q.getStatus())
                .collect(Collectors.toList());
        m.addAttribute("quizList",quizList);



        return "Admin/quizquestion/CrudQuizQuestion";

    }
        @PostMapping("create")
        public String create(Model m, HttpServletRequest request
                       ) {
            String quizz =request.getParameter("quiz");
            int idquiz =Integer.parseInt(quizz);
            Quiz quiz = quizDAO.layquizid(idquiz);

            for (int i = 1; i <= 10; i++) {
                QuizQuestion question = new QuizQuestion();
                question.setQuestion(request.getParameter("question" + i));
                question.setOptionA(request.getParameter("optionA" + i));
                question.setOptionB(request.getParameter("optionB" + i));
                question.setOptionC(request.getParameter("optionC" + i));
                question.setOptionD(request.getParameter("optionD" + i));
                question.setCorrectoption(request.getParameter("correctoption" + i));
                question.setQuizQues(quiz);
                quizQuestionDAO.save(question);
            }
            quiz.setStatus(true);
            quizDAO.save(quiz);
            return "redirect:/admin/quiz";

        }
        @GetMapping("edit")
        public String edit(Model m, HttpServletRequest request){
        String id =request.getParameter("id");
        int idquiz =Integer.parseInt(id);
        List<QuizQuestion>quizQuestions=quizQuestionDAO.timidquiz(idquiz);
        m.addAttribute("quizQuestions",quizQuestions);
        Quiz quizList = quizDAO.getById(idquiz);
        m.addAttribute("quizList",quizList);
            return "Admin/quizquestion/editQuizQuestion";

        }
        @PostMapping("update")
        public String save(Model m, HttpServletRequest request
        ) {
            String quizz =request.getParameter("quiz");
            int idquiz =Integer.parseInt(quizz);
            Quiz quiz = quizDAO.layquizid(idquiz);
            List<QuizQuestion> questions = quizQuestionDAO.timidquiz(idquiz);
            for (int i = 1; i <= 10; i++) {
                QuizQuestion question = questions.get(i - 1);
                question.setQuestion(request.getParameter("question" + i));
                question.setOptionA(request.getParameter("optionA" + i));
                question.setOptionB(request.getParameter("optionB" + i));
                question.setOptionC(request.getParameter("optionC" + i));
                question.setOptionD(request.getParameter("optionD" + i));
                question.setCorrectoption(request.getParameter("correctoption" + i));
                question.setQuizQues(quiz);
                quizQuestionDAO.save(question);
            }
            return "redirect:/admin/quiz";

        }



}

package com.pts.controller.admin;

import com.pts.dao.AccountDAO;
import com.pts.dao.CourseCodeDAO;
import com.pts.dao.CourseDAO;
import com.pts.entity.Account;
import com.pts.entity.AccountPage;
import com.pts.entity.Course;
import com.pts.entity.CourseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Random;

@Controller
@RequestMapping("/admin/code-khoahoc")
public class QuanLyMaCodeController {
    private final int pageSize = 8;
    @Autowired
    CourseCodeDAO courseCodeDAO;

    @Autowired
    CourseDAO courseDAO;

    @Autowired
    AccountDAO accountDAO;

    @GetMapping
    public String index(Model m, @RequestParam(defaultValue = "1") int page,
                        @RequestParam(defaultValue = "") String keyword) {
        List<CourseCode> courseCodes;
        if (keyword == "") {
            courseCodes = courseCodeDAO.findAll();
            m.addAttribute("course", courseCodes);

        } else {
            courseCodes = courseCodeDAO.timkiemTheoKhoaHoc(keyword);
        }

        int totalAccounts = courseCodes.size();
        int totalPages = (int) Math.ceil(totalAccounts / (double) pageSize);

        // Lấy danh sách tài khoản trên trang hiện tại
        int start = (page - 1) * pageSize;

        int end = Math.min(start + pageSize, totalAccounts);
        List<CourseCode> accountsOnPage = courseCodes.subList(start, end);

        // Đưa thông tin về dữ liệu và phân trang vào Model
        AccountPage accountPage = new AccountPage();
        accountPage.setCourseCodes(accountsOnPage);
        accountPage.setTotalPages(totalPages);
        accountPage.setCurrentPage(page);
        m.addAttribute("accountPage", accountPage);
        List<Course> courses = courseDAO.findAll();
        m.addAttribute("courses", courses);

        return "/Admin/codeKH";

    }

    @PostMapping("/create")
    public String create(
                         @RequestParam("course") int course,
                         @RequestParam("quantity") int quantity, HttpServletRequest request) {

        CourseCode courseCode = new CourseCode();
        Account acc = accountDAO.laytk(request.getRemoteUser());
        Course courses = courseDAO.findById(course);
        courseCode.setAccount(acc);
        courseCode.setQuantity(quantity);
        courseCode.setCourseCode(courses);
        Random random = new Random();
        int randomNumber = random.nextInt(1000000000); // Sinh số nguyên ngẫu nhiên từ 0 đến 99999999
        String nameWithoutSpaces = courses.getTps_Name().replaceAll("[^a-zA-Z0-9]", "");
        String code = nameWithoutSpaces + String.format("%08d", randomNumber);
        courseCode.setStatus(true);
        courseCode.setCode(code);
        System.out.println(code);
        courseCodeDAO.save(courseCode);
        return "redirect:/admin/code-khoahoc";


    }
    @GetMapping("/edit")
    public String edit(Model m,@RequestParam("idcode")int id, @RequestParam(defaultValue = "1") int page,
                       @RequestParam(defaultValue = "") String keyword){

        CourseCode courseCode=courseCodeDAO.timcode(id);
        m.addAttribute("edit",courseCode);
        List<CourseCode> courseCodes;
        if (keyword == "") {
            courseCodes = courseCodeDAO.findAll();
            m.addAttribute("course", courseCodes);

        } else {
            courseCodes = courseCodeDAO.timkiemTheoKhoaHoc(keyword);
        }

        int totalAccounts = courseCodes.size();
        int totalPages = (int) Math.ceil(totalAccounts / (double) pageSize);

        // Lấy danh sách tài khoản trên trang hiện tại
        int start = (page - 1) * pageSize;

        int end = Math.min(start + pageSize, totalAccounts);
        List<CourseCode> accountsOnPage = courseCodes.subList(start, end);

        // Đưa thông tin về dữ liệu và phân trang vào Model
        AccountPage accountPage = new AccountPage();
        accountPage.setCourseCodes(accountsOnPage);
        accountPage.setTotalPages(totalPages);
        accountPage.setCurrentPage(page);
        m.addAttribute("accountPage", accountPage);
        List<Course>courses=courseDAO.findAll();
        m.addAttribute("courses",courses);
        return "/Admin/codeKH";

    }
    @PostMapping("/save")
    public String save(Model m, @RequestParam("id")int id, @RequestParam("code")String code,
                       @RequestParam("course")int course, @RequestParam("quantity")int quantity,
                       @RequestParam("radio")Boolean radio, RedirectAttributes redirectAttributes){

        Course courses=courseDAO.findById(course);
        CourseCode courseCode=courseCodeDAO.timcode(id);
        courseCode.setStatus(radio);
        courseCode.setCode(code);
        courseCode.setQuantity(quantity);
        courseCode.setCourseCode(courses);
        courseCodeDAO.save(courseCode);

        redirectAttributes.addFlashAttribute("success", "Update successful!");
        return "redirect:/admin/code-khoahoc/edit?idcode="+id ;
    }

}
package com.pts.controller.admin;

import com.pts.dao.ChapterDAO;
import com.pts.dao.CourseDAO;
import com.pts.entity.AccountPage;
import com.pts.entity.Chapter;
import com.pts.entity.Course;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/chuong")
public class ChuongController {

    @Autowired
    ChapterDAO chapterDAO;

    @Autowired
    CourseDAO courseDAO;
    private final int pageSize = 8;

    @GetMapping
    public String index(Model m, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "") String keyword,
                        @RequestParam(defaultValue = "chuong") String radio) {

        List<Chapter> chapterList;

        if (keyword.equals("")) {
            chapterList = chapterDAO.findAll();
        } else {
            if (radio.equals("chuong")) {
                chapterList = chapterDAO.timkiem(keyword);
            } else {
                chapterList = chapterDAO.timkiemKhoaHoc(keyword);
            }
        }

        int totalAccounts = chapterList.size();
        int totalPages = (int) Math.ceil(totalAccounts / (double) pageSize);

        // Lấy danh sách tài khoản trên trang hiện tại
        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, totalAccounts);
        List<Chapter> accountsOnPage = chapterList.subList(start, end);
        List<Course> courses = courseDAO.findAll();

        // Đưa thông tin về dữ liệu và phân trang vào Model
        AccountPage accountPage = new AccountPage();
        accountPage.setChapters(accountsOnPage);
        accountPage.setTotalPages(totalPages);
        accountPage.setCurrentPage(page);
        m.addAttribute("accountPage", accountPage);
        m.addAttribute("courses", courses);

        return "Admin/chuong";
    }

    @RequestMapping("/edit")
    public String index(Model m,@RequestParam("id")int id,@RequestParam(defaultValue = "1") int page,
                            @RequestParam(defaultValue = "") String keyword){
        Chapter chapter=chapterDAO.find(id);
        m.addAttribute("chapter",chapter);

            List<Chapter> chapterList;
            if(keyword.equals("")){
                chapterList=chapterDAO.findAll();
            }else{
                chapterList = chapterDAO.timkiem(keyword);
            }

            int totalAccounts = chapterList.size();
            int totalPages = (int) Math.ceil(totalAccounts / (double) pageSize);

            // Lấy danh sách tài khoản trên trang hiện tại
            int start = (page - 1) * pageSize;

            int end = Math.min(start + pageSize, totalAccounts);
            List<Chapter> accountsOnPage = chapterList.subList(start, end);

            // Đưa thông tin về dữ liệu và phân trang vào Model
            AccountPage accountPage = new AccountPage();
            accountPage.setChapters(accountsOnPage);
            accountPage.setTotalPages(totalPages);
            accountPage.setCurrentPage(page);
            m.addAttribute("accountPage", accountPage);

            return "Admin/chuong";

        }

        @PostMapping("/save")
        public String save(Model m, @RequestParam("id")int id, RedirectAttributes redirectAttributes,
                           @RequestParam("title")String title,
                           @RequestParam("course_name")String course_name){
        Chapter chapter= chapterDAO.find(id);
        chapter.setTps_title(title);
        chapterDAO.save(chapter);
        return "redirect:/admin/chuong/edit?id="+id;
        }

        @PostMapping("/create")
        public String create(Model m,@RequestParam("name")String name,@RequestParam("id")int id){
        Chapter chapter = new Chapter();
        Course course=courseDAO.findById(id);
        chapter.setTps_title(name);
        chapter.setCourse(course);
        chapterDAO.save(chapter);

        return "redirect:/admin/chuong";
        }

        
}

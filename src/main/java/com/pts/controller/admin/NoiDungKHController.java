package com.pts.controller.admin;

import com.cloudinary.Cloudinary;
import com.pts.dao.ChapterDAO;
import com.pts.dao.ContentDAO;
import com.pts.dao.CourseDAO;
import com.pts.dao.QuizDAO;
import com.pts.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/admin/noi-dung")
public class NoiDungKHController {


    @Autowired
    QuizDAO quizDAO;
    @Autowired
    ContentDAO contentDAO;

    @Autowired
    CourseDAO courseDAO;
    @Autowired
    ChapterDAO chapterDAO;
    
    @Autowired
    Cloudinary cloudinary;
    private final int pageSize = 8;
    @GetMapping
    public String index(Model m, @RequestParam(defaultValue = "1") int page,
                        @RequestParam(defaultValue = "") String keyword,
                        @RequestParam(defaultValue = "") String option,
                        @RequestParam(defaultValue = "") String courseId,
                        @RequestParam(defaultValue = "") String name){

        List<Content> contents;
        if(keyword ==""){
            contents=contentDAO.findAll();
        }else{
            if (option.equals("KH")) {
                contents = contentDAO.timkiemTheoKhoaHoc(keyword);
            } else if (option.equals("C")) {
                contents = contentDAO.timkiemTheoChuong(keyword);
            } else {
                contents = contentDAO.timkiem(keyword);
            }
        }

        int totalAccounts = contents.size();
        int totalPages = (int) Math.ceil(totalAccounts / (double) pageSize);

        // Lấy danh sách tài khoản trên trang hiện tại
        int start = (page - 1) * pageSize;

        int end = Math.min(start + pageSize, totalAccounts);
        List<Content> accountsOnPage = contents.subList(start, end);

        // Đưa thông tin về dữ liệu và phân trang vào Model
        AccountPage accountPage = new AccountPage();
        accountPage.setContents(accountsOnPage);
        accountPage.setTotalPages(totalPages);
        accountPage.setCurrentPage(page);
        m.addAttribute("accountPage", accountPage);
        List<Course>courses=courseDAO.findAll();
        m.addAttribute("courses",courses);
        if(courseId.equals("")){

        }else{
        	
            int idc=Integer.parseInt(courseId);
            Course course=courseDAO.findById(idc);
            List<Chapter>chapters =chapterDAO.findByCourse(course.getTps_id());
            m.addAttribute("chapter",chapters);
            Course courses2 =courseDAO.findById(idc);
            m.addAttribute("thongtin",courses2);
            m.addAttribute("name",name);
        }

        return "/Admin/noidungKH";
    }
    @PostMapping("/create")
    public String create(Model m,@RequestParam("mulimage") MultipartFile file,
                         @RequestParam("tenbaihoc")String name,
                         @RequestParam("course")int course,
                         @RequestParam("chapter")int chapter) throws IOException {
        Course courses=courseDAO.findById(course);
    	 Content contents = new Content();
         Chapter chapters = chapterDAO.find(chapter);



         // Thiết lập tham số upload
         Map<String, Object> uploadParams = new HashMap<>();
         String uniqueFileName = file.getOriginalFilename() + "-" + UUID.randomUUID().toString();
         uploadParams.put("folder", "samples");
         uploadParams.put("public_id", uniqueFileName);
         uploadParams.put("resource_type", "video");
         uploadParams.put("chunk_size", 100000000);
         uploadParams.put("timeout", 120000);

         // Upload file và lấy ra mã lực thông tin
         Map<String, Object> result = cloudinary.uploader().upload(file.getBytes(), uploadParams);
         String url = result.get("url").toString();

        contents.setChapter(chapters);
        contents.setTps_title(name);
         contents.setTps_linkytb(url);

        Content savedContent= contentDAO.save(contents);

        Quiz quiz = new Quiz();
        quiz.setContent(savedContent);
        quiz.setCourse(courses);
        quiz.setStatus(false);
        quizDAO.save(quiz);
        return "redirect:/admin/noi-dung";
    }
    @GetMapping("/edit")
    public String edit(Model m,@RequestParam("id")int id, @RequestParam(defaultValue = "1") int page,
                       @RequestParam(defaultValue = "") String keyword,
                       @RequestParam(defaultValue = "") String option,
                       @RequestParam(defaultValue = "") String courseId,
                       @RequestParam(defaultValue = "") String name){
        Content content =contentDAO.connn(id);
        m.addAttribute("content",content);
        List<Content> contents;
        if(keyword ==""){
            contents=contentDAO.findAll();
        }else{
            if (option.equals("KH")) {
                contents = contentDAO.timkiemTheoKhoaHoc(keyword);
            } else if (option.equals("C")) {
                contents = contentDAO.timkiemTheoChuong(keyword);
            } else {
                contents = contentDAO.timkiem(keyword);
            }
        }

        int totalAccounts = contents.size();
        int totalPages = (int) Math.ceil(totalAccounts / (double) pageSize);

        // Lấy danh sách tài khoản trên trang hiện tại
        int start = (page - 1) * pageSize;

        int end = Math.min(start + pageSize, totalAccounts);
        List<Content> accountsOnPage = contents.subList(start, end);

        // Đưa thông tin về dữ liệu và phân trang vào Model
        AccountPage accountPage = new AccountPage();
        accountPage.setContents(accountsOnPage);
        accountPage.setTotalPages(totalPages);
        accountPage.setCurrentPage(page);
        m.addAttribute("accountPage", accountPage);
        List<Course>courses=courseDAO.findAll();
        m.addAttribute("courses",courses);

        return "/Admin/noidungKH";
    }

    @PostMapping("/save")
    public String save(Model m,@RequestParam("image")String urlvideo,
                       @RequestParam("mulimage") MultipartFile file,
                       @RequestParam("name")String name,
                       @RequestParam("chapter")int chapter,
                       @RequestParam("id")int id)throws IOException{
        String url = null;
        if (file != null && !file.isEmpty()) {
            Map<String, Object> uploadParams = new HashMap<>();
            String uniqueFileName = file.getOriginalFilename() + "-" + UUID.randomUUID().toString();
            uploadParams.put("folder", "samples");
            uploadParams.put("public_id", uniqueFileName);
            uploadParams.put("resource_type", "video");
            uploadParams.put("chunk_size", 100000000);
            uploadParams.put("timeout", 120000);

            // Upload file và lấy ra mã lực thông tin
            Map<String, Object> result = cloudinary.uploader().upload(file.getBytes(), uploadParams);
            url= result.get("url").toString();
        } else {
            url = urlvideo;
        }
        Chapter chapters = chapterDAO.find(chapter);
        Content content= contentDAO.connn(id);
        content.setTps_title(name);
        content.setTps_linkytb(url);
        content.setChapter(chapters);
        contentDAO.save(content);
        return "redirect:/admin/noi-dung/edit?id="+id;

    }



}

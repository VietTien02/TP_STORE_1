package com.pts.controller.admin;

import com.cloudinary.Cloudinary;
import com.pts.dao.ChapterDAO;
import com.pts.dao.ContentDAO;
import com.pts.dao.CourseDAO;
import com.pts.dao.QuizDAO;
import com.pts.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
                        @RequestParam(defaultValue = "") String name) {

        List<Content> contents;
        if (keyword == "") {
            contents = contentDAO.findAll();
        } else {
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
        List<Course> courses = courseDAO.findAll();
        m.addAttribute("courses", courses);
        if (courseId.equals("")) {

        } else {

            int idc = Integer.parseInt(courseId);
            Course course = courseDAO.findById(idc);
            List<Chapter> chapters = chapterDAO.findByCourse(course.getTps_id());
            m.addAttribute("chapter", chapters);
            Course courses2 = courseDAO.findById(idc);
            m.addAttribute("thongtin", courses2);
            m.addAttribute("name", name);
        }
        return "/Admin/noidungKH";
    }

    @PostMapping("/create")
    public ResponseEntity<String> create(Model m, @RequestParam("mulimage") MultipartFile file,
                                         @RequestParam("tenbaihoc") String name,
                                         @RequestParam("course") String courseStr,
                                         @RequestParam("chapter") String chapterStr) throws IOException {
        int course = 0, chapter = 0;

        if (name.isEmpty() || name == null || courseStr.isEmpty() || courseStr == null || chapterStr.isEmpty() || chapterStr == null) {
            return ResponseEntity.badRequest().body("Vui lòng nhập đầy đủ thông tin !");
        }else {
            course = Integer.parseInt(courseStr);
            chapter = Integer.parseInt(chapterStr);
        }
        Course courses = courseDAO.findById(course);
        Chapter chapters = chapterDAO.find(chapter);
        Content contents = new Content();

        if (file.isEmpty() || file == null) {
            return ResponseEntity.badRequest().body("Vui lòng thêm video nội dung bài học !");
        }
        String fileName = file.getOriginalFilename();
        System.out.println("name: " + fileName);
        if (!file.isEmpty() && !(fileName.toLowerCase().endsWith(".mp4") || fileName.toLowerCase().endsWith(".avi") || fileName.toLowerCase().endsWith(".mov"))) {
            return ResponseEntity.badRequest().body("Video không đúng định dạng. Vui lòng kiểm tra dạng phải là mp4 , avi, hoặc mov");
        }
        Content checkContentExists = contentDAO.timTenNoiDungKhoaHocTheoChuong(name, chapter);
        if(checkContentExists != null ){
            return ResponseEntity.badRequest().body("Nội dung bài học đã tồn tại !");
        }
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

        Content savedContent = contentDAO.save(contents);

        Quiz quiz = new Quiz();
        quiz.setContent(savedContent);
        quiz.setCourse(courses);
        quiz.setStatus(false);
        quizDAO.save(quiz);
        return ResponseEntity.ok("Thêm nội dung bài học thành công !");
    }

    @GetMapping("/edit")
    public String edit(Model m, @RequestParam("id") int id, @RequestParam(defaultValue = "1") int page,
                       @RequestParam(defaultValue = "") String keyword,
                       @RequestParam(defaultValue = "") String option,
                       @RequestParam(defaultValue = "") String courseId,
                       @RequestParam(defaultValue = "") String name) {
        Content content = contentDAO.connn(id);
        m.addAttribute("content", content);
        List<Content> contents;
        if (keyword == "") {
            contents = contentDAO.findAll();
        } else {
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
        List<Course> courses = courseDAO.findAll();
        m.addAttribute("courses", courses);

        return "/Admin/noidungKH";
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(Model m, @RequestParam("image") String urlvideo,
                       @RequestParam("mulimage") MultipartFile file,
                       @RequestParam("name") String name,
                       @RequestParam("chapter") int chapter,
                       @RequestParam("id") int id) throws IOException {
        String url = null;
        if (name.isEmpty() || name == null ) {
            return ResponseEntity.badRequest().body("Vui lòng nhập đầy đủ thông tin !");
        }
        String fileName = file.getOriginalFilename();
        System.out.println("name: " + fileName);
        if (!file.isEmpty() && !(fileName.toLowerCase().endsWith(".mp4") || fileName.toLowerCase().endsWith(".avi") || fileName.toLowerCase().endsWith(".mov"))) {
            return ResponseEntity.badRequest().body("Video không đúng định dạng. Vui lòng kiểm tra dạng phải là mp4 , avi, hoặc mov");
        }
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
            url = result.get("url").toString();
        } else {
            url = urlvideo;
        }
        Chapter chapters = chapterDAO.find(chapter);
        Content content = contentDAO.connn(id);
        content.setTps_title(name);
        content.setTps_linkytb(url);
        content.setChapter(chapters);
        contentDAO.save(content);
        return ResponseEntity.ok("Sua thanh cong !");

    }


}

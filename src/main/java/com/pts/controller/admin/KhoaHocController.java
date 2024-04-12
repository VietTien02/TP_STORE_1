package com.pts.controller.admin;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.pts.dao.CategoryDAO;
import com.pts.dao.CourseDAO;
import com.pts.entity.AccountPage;
import com.pts.entity.Category;
import com.pts.entity.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("admin/khoa-hoc")
public class KhoaHocController {
    @Autowired
    CourseDAO courseDAO;
    @Autowired
    CategoryDAO categoryDAO;
    @Autowired
    private Cloudinary cloudinary;

    private final int pageSize = 8;
    @GetMapping
    public String index(Model m,@RequestParam(defaultValue = "1") int page,
                        @RequestParam(defaultValue = "") String keyword){
        List<Category> categories=categoryDAO.findAll();
        m.addAttribute("category",categories);

        List<Course> courseList;
        if(keyword ==""){
            courseList =courseDAO.findAll();
            m.addAttribute("course",courseList);

        }else{
            courseList = courseDAO.searchCourses(keyword);
        }

        int totalAccounts = courseList.size();
        int totalPages = (int) Math.ceil(totalAccounts / (double) pageSize);

        // Lấy danh sách tài khoản trên trang hiện tại
        int start = (page - 1) * pageSize;

        int end = Math.min(start + pageSize, totalAccounts);
        List<Course> accountsOnPage = courseList.subList(start, end);

        // Đưa thông tin về dữ liệu và phân trang vào Model
        AccountPage accountPage = new AccountPage();
        accountPage.setCourses(accountsOnPage);
        accountPage.setTotalPages(totalPages);
        accountPage.setCurrentPage(page);
        m.addAttribute("accountPage", accountPage);
        return "Admin/KhoaHoc";

    }
        @GetMapping("/edit")
        public String edit(Model m,@RequestParam("id")int id,
                           @RequestParam(defaultValue = "1") int page,
                           @RequestParam(defaultValue = "") String keyword){
        Course course =courseDAO.findById(id);
        m.addAttribute("course",course);
        List<Category>categories=categoryDAO.findAll();
            m.addAttribute("category",categories);

            String url = course.getLearn();
            try {
                URL cloudinaryUrl = new URL(url);
                URLConnection connection = cloudinaryUrl.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                reader.close();
                String fileContent = sb.toString();
                m.addAttribute("fileContent", fileContent);

            } catch (IOException e) {
                System.out.println("Error reading file from Cloudinary: " + e.getMessage());
            }


            List<Course> courseList;
            if(keyword ==""){
                courseList =courseDAO.findAll();
                m.addAttribute("course",courseList);

            }else{
                courseList = courseDAO.searchCourses(keyword);
            }

            int totalAccounts = courseList.size();
            int totalPages = (int) Math.ceil(totalAccounts / (double) pageSize);

            // Lấy danh sách tài khoản trên trang hiện tại
            int start = (page - 1) * pageSize;

            int end = Math.min(start + pageSize, totalAccounts);
            List<Course> accountsOnPage = courseList.subList(start, end);

            // Đưa thông tin về dữ liệu và phân trang vào Model
            AccountPage accountPage = new AccountPage();
            accountPage.setCourses(accountsOnPage);
            accountPage.setTotalPages(totalPages);
            accountPage.setCurrentPage(page);
            m.addAttribute("accountPage", accountPage);
        return "Admin/KhoaHoc";

        }

        @PostMapping("/save")
        public String save(Model m,
                           @RequestParam("id")int id,
                           @RequestParam("name")String name,
                           @RequestParam("discount")int discount,
                           @RequestParam("price")int price,
                           @RequestParam("information")String information,
                           @RequestParam("noidung")String noidung,
                           @RequestParam("mulimage") MultipartFile file,
                           @RequestParam("image")String image,
                           @RequestParam("url")String url,
                           @RequestParam("radio")String radio,
                           @RequestParam("category")int categoty) throws IOException {
            System.out.println(categoty);

            Course course =courseDAO.findById(id);
            Category category =categoryDAO.getById(categoty);
            String urlImgae = null;
            if (file != null && !file.isEmpty()) {
                Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("folder", "photo"));
                urlImgae = uploadResult.get("url").toString();
            } else {
                urlImgae = image;
            }
            String folderName = "noidung";
            Map uploadResult = cloudinary.uploader().upload(noidung.getBytes(), ObjectUtils.asMap(
                    "resource_type", "raw",
                    "format", "txt",
                    "folder", folderName
            ));
            // Lấy đường dẫn của file txt đã upload
            String txtUrl = uploadResult.get("secure_url").toString();

            if(radio.equals("true")){
                course.setTps_Status(true);
            }else{
                course.setTps_Status(false);
            }
            course.setTps_Name(name);   
            course.setTps_image(urlImgae);
            course.setTps_discount(discount);
            course.setTps_Price(price);
            course.setTps_information(information);
            course.setLearn(txtUrl);
            course.setCategory(category);
            courseDAO.save(course);

            return "redirect:/admin/khoa-hoc/edit?id="+id;
        }

        @PostMapping("/create")
        public String create(@RequestParam("name")String name,
                             @RequestParam("discount")int discount,
                             @RequestParam("price")int price,
                             @RequestParam("information")String information,
                             @RequestParam("noidung")String noidung,
                             @RequestParam("image") MultipartFile file,

                             @RequestParam("radio")String radio,
                             @RequestParam("category")int idcategory,
                             @RequestParam(defaultValue = "1") int page,
                             @RequestParam(defaultValue = "") String keyword,
                             Model m) throws IOException {

            Course course =new Course();
            Category category =new Category();
            category.setTps_Id(idcategory);


            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("folder", "photo"));
            String urlImgae = uploadResult.get("url").toString();
            String folderName = "noidung";
            Map uploadResult2 = cloudinary.uploader().upload(noidung.getBytes(), ObjectUtils.asMap(
                    "resource_type", "raw",
                    "format", "txt",
                    "folder", folderName
            ));
            // Lấy đường dẫn của file txt đã upload
            String txtUrl = uploadResult2.get("secure_url").toString();

            if(radio.equals("true")){
                course.setTps_Status(true);
            }else{
                course.setTps_Status(false);
            }
            course.setTps_Name(name);
            course.setTps_image(urlImgae);
            course.setTps_discount(discount);
            course.setTps_Price(price);
            course.setTps_information(information);
            course.setLearn(txtUrl);
            course.setCategory(category);
            courseDAO.save(course);

            List<Course> courseList;
            if(keyword ==""){
                courseList =courseDAO.findAll();
                m.addAttribute("course",courseList);

            }else{
                courseList = courseDAO.searchCourses(keyword);
            }

            int totalAccounts = courseList.size();
            int totalPages = (int) Math.ceil(totalAccounts / (double) pageSize);

            // Lấy danh sách tài khoản trên trang hiện tại
            int start = (page - 1) * pageSize;

            int end = Math.min(start + pageSize, totalAccounts);
            List<Course> accountsOnPage = courseList.subList(start, end);

            // Đưa thông tin về dữ liệu và phân trang vào Model
            AccountPage accountPage = new AccountPage();
            accountPage.setCourses(accountsOnPage);
            accountPage.setTotalPages(totalPages);
            accountPage.setCurrentPage(page);
            m.addAttribute("accountPage", accountPage);
            return "Admin/KhoaHoc";
        }
        @PostMapping("/reset")
        public String reset(Model m){
        return "redirect:/admin/khoa-hoc";
        }
}

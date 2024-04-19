package com.pts.controller.admin;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.pts.dao.CategoryDAO;
import com.pts.dao.CourseDAO;
import com.pts.entity.AccountPage;
import com.pts.entity.Category;
import com.pts.entity.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
        public ResponseEntity<String> save(Model m,
                           @RequestParam("id")int id,
                           @RequestParam("name")String name,
                           @RequestParam(value = "discount", defaultValue = "")String discountStr,
                           @RequestParam(value = "price", defaultValue = "")String priceStr,
                           @RequestParam("information")String information,
                           @RequestParam("noidung")String noidung,
                           @RequestParam("image") MultipartFile file,
                           @RequestParam("url")String url,
                           @RequestParam("radio")String radio,
                           @RequestParam("category")String categotyStr) throws IOException {
            System.out.println(categotyStr);
            if (name.trim().isEmpty() || information.trim().isEmpty() || noidung.trim().isEmpty() || radio.trim().isEmpty() || categotyStr.isEmpty()) {
                return ResponseEntity.badRequest().body("Vui lòng nhập đầy đủ thông tin !");
            }

            long maxSize = 5 * 1024 * 1024; // 5 MB
            if (file.getSize() > maxSize) {
                return ResponseEntity.badRequest().body("Kích thước tệp tin không được vượt quá 5 MB!");
            }
            int discount = 0, price = 0, categoty = 0;
            if(!discountStr.trim().isEmpty() ){
                discount = Integer.parseInt(discountStr);
            }
            if(!priceStr.trim().isEmpty()){
                price = Integer.parseInt(priceStr);
            }if (!categotyStr.trim().isEmpty()){
                categoty = Integer.parseInt(categotyStr);
            }
            Course course =courseDAO.findById(id);
            Category category =categoryDAO.getById(categoty);
            String urlImgae = null;
            if (file != null && !file.isEmpty()) {
                Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("folder", "photo"));
                urlImgae = uploadResult.get("url").toString();
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

            return ResponseEntity.ok("Sửa khóa học thành công !");
        }

        @PostMapping("/create")
        public ResponseEntity<String> create(@RequestParam(value = "name", defaultValue = "")String name,
                                     @RequestParam(value = "discount", defaultValue = "0")String discountStr,
                                     @RequestParam(value = "price", defaultValue = "0")String priceStr,
                                     @RequestParam(value = "information", defaultValue = "")String information,
                                     @RequestParam(value = "noidung", defaultValue = "")String noidung,
                                     @RequestParam(value = "image", defaultValue = "") MultipartFile file,
                                     @RequestParam(value = "radio", defaultValue = "")String radio,
                                     @RequestParam(value = "category")String category_id_str,
                                     @RequestParam(defaultValue = "1") int page,
                                     @RequestParam(defaultValue = "") String keyword,
                                     Model m) throws IOException {
            Course checkCouseExists = courseDAO.findByName(name);
            System.out.println("Hahaha chạy vào đây rồi !");
            System.out.println("File: " + file.getContentType());
            if(checkCouseExists != null){
                return ResponseEntity.badRequest().body("Khóa học đã tồn tại: ");
            }
            System.out.println("Hahaha chạy vào đây rồi !");
            //int d = category_id;
            //System.out.println("Discount: " + discount.g);
//            String discountString = Integer.toString(discount);
//            String priceString = Integer.toString(price);
//            String categotyString = Integer.toString(category_id);
            System.out.println("Discount = " + discountStr + " \n Prince: " + priceStr);

            if (name.trim().isEmpty() || information.trim().isEmpty() || noidung.trim().isEmpty() || radio.trim().isEmpty() || category_id_str.isEmpty()) {
                return ResponseEntity.badRequest().body("Vui lòng nhập đầy đủ thông tin !");
            }
            if (!file.getContentType().equals(MediaType.IMAGE_JPEG_VALUE) && !file.getContentType().equals(MediaType.IMAGE_PNG_VALUE) && !file.getContentType().equals(MediaType.IMAGE_GIF_VALUE)) {
                return ResponseEntity.badRequest().body("Tệp tin phải là hình ảnh hoặc GIF!");
            }
            long maxSize = 5 * 1024 * 1024; // 5 MB
            if (file.getSize() > maxSize) {
                return ResponseEntity.badRequest().body("Kích thước tệp tin không được vượt quá 5 MB!");
            }
            int discount = 0, price = 0 , category_id = 0 ;
            if (!discountStr.trim().isEmpty()  ){
                discount = Integer.parseInt(discountStr);
            }
            if (!priceStr.trim().isEmpty()){
                price = Integer.parseInt(priceStr);
            }
            if(!category_id_str.isEmpty()){
                category_id = Integer.parseInt(category_id_str);
            }
            System.out.println("Discount = " + discount + " \n Prince: " + price);

            if(file.isEmpty() == true){
                return ResponseEntity.badRequest().body("Vui lòng chọn một file !");
            }
            Course course =new Course();
            Category category =new Category();
            category.setTps_Id(category_id);
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
            return ResponseEntity.ok("Thêm tài khóa học thành công !");
        }
        @PostMapping("/reset")
        public String reset(Model m){
        return "redirect:/admin/khoa-hoc";
        }
}

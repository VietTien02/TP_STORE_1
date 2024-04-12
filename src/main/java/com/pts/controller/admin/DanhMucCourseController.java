package com.pts.controller.admin;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.pts.dao.CategoryDAO;
import com.pts.entity.AccountPage;
import com.pts.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/danh-muc")
public class DanhMucCourseController {

    @Autowired
    CategoryDAO categoryDAO;

    @Autowired
    private Cloudinary cloudinary;

    private final int pageSize = 8;

    @GetMapping
    public String danhmuc(Model m,@RequestParam(defaultValue = "1") int page,
                          @RequestParam(defaultValue = "") String keyword){
        List<Category> categoryList;
        if (keyword.equals("")) {
            categoryList =categoryDAO.findAll();
        } else {

            categoryList = categoryDAO.timkiem(keyword);
        }
        m.addAttribute("category",categoryList);
        int totalAccounts = categoryList.size();
        int totalPages = (int) Math.ceil(totalAccounts / (double) pageSize);

        // Lấy danh sách tài khoản trên trang hiện tại
        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, totalAccounts);
        List<Category> accountsOnPage = categoryList.subList(start, end);

        // Đưa thông tin về dữ liệu và phân trang vào Model
        AccountPage accountPage = new AccountPage();
        accountPage.setCategories(categoryList);
        accountPage.setTotalPages(totalPages);
        accountPage.setCurrentPage(page);
        m.addAttribute("accountPage", accountPage);

        return "/Admin/danhMuc";

    }
    @GetMapping("/edit")
    public String edit(Model m, @RequestParam("id")int id,@RequestParam(defaultValue = "1") int page,
                       @RequestParam(defaultValue = "") String keyword){
        Category category =categoryDAO.getById(id);
        m.addAttribute("edit",category);

        List<Category> categoryList;
        if (keyword.equals("")) {
            categoryList =categoryDAO.findAll();
        } else {

            categoryList = categoryDAO.timkiem(keyword);
        }
        m.addAttribute("category",categoryList);
        int totalAccounts = categoryList.size();
        int totalPages = (int) Math.ceil(totalAccounts / (double) pageSize);

        // Lấy danh sách tài khoản trên trang hiện tại
        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, totalAccounts);
        List<Category> accountsOnPage = categoryList.subList(start, end);

        // Đưa thông tin về dữ liệu và phân trang vào Model
        AccountPage accountPage = new AccountPage();
        accountPage.setCategories(categoryList);
        accountPage.setTotalPages(totalPages);
        accountPage.setCurrentPage(page);
        m.addAttribute("accountPage", accountPage);

        return "/Admin/danhMuc";
    }

    @PostMapping("/save")
    public String save(Model m,@RequestParam("name")String name,
                       @RequestParam(value = "mulimage", required = false) MultipartFile file,
                       @RequestParam("id")int id,@RequestParam("image")String image) throws IOException {

        String url = null;
        if (file != null && !file.isEmpty()) {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("folder", "photo"));
            url = uploadResult.get("url").toString();
        } else {
            url = image;
        }

        Category category = categoryDAO.getById(id);
        category.setTps_Name(name);
        category.setTps_Image(url);
        categoryDAO.save(category);

        return "redirect:/admin/danh-muc/edit?id="+id;

    }
    @PostMapping("/create")
    public String create(Model m,@RequestParam("name")String name,
                         @RequestParam(value = "mulimage", required = false) MultipartFile file
                        ) throws IOException {
        Category category=new Category();
        category.setTps_Name(name);

        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("folder", "photo"));
        String url = uploadResult.get("url").toString();
        category.setTps_Image(url);
        categoryDAO.save(category);
        return "redirect:/admin/danh-muc";
    }

        @RequestMapping("/reset")
    public String reset(Model m){
        return "redirect:/admin/danh-muc";
    }
}




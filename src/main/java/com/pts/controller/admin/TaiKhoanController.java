package com.pts.controller.admin;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.pts.dao.AccountDAO;

import com.pts.entity.Account;


import com.pts.entity.AccountPage;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/admin/tai-khoan")
public class TaiKhoanController {

    @Autowired
    AccountDAO accountDAO;
    @Autowired
    private Cloudinary cloudinary;

    private final int pageSize = 8;

    @GetMapping
    public String danhSachTaiKhoan(@RequestParam(defaultValue = "1") int page,
                                   @RequestParam(defaultValue = "") String keyword,
                                   Model model) {
        List<Account> accounts;
        if(keyword.equals("")){
            accounts = accountDAO.findAll();
        }else{
            accounts = accountDAO.timkiemacc(keyword);
        }

        int totalAccounts = accounts.size();
        int totalPages = (int) Math.ceil(totalAccounts / (double) pageSize);

        // Lấy danh sách tài khoản trên trang hiện tại
        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, totalAccounts);

        List<Account> accountsOnPage = accounts.subList(start, end);

        // Đưa thông tin về dữ liệu và phân trang vào Model
        AccountPage accountPage = new AccountPage();
        accountPage.setAccounts(accountsOnPage);
        accountPage.setTotalPages(totalPages);
        accountPage.setCurrentPage(page);
        model.addAttribute("accountPage", accountPage);


        return "Admin/qltk";
    }
    @GetMapping("/lay-tai-khoan")

    public List<Account> getAccountDAO() {
        return accountDAO.findAll();
    }

    @GetMapping("/edit")
    public String edit(Model m,@RequestParam("username")String username,@RequestParam(defaultValue = "1") int page,
                       @RequestParam(defaultValue = "") String keyword){
        Account acc =accountDAO.laytk(username);
        m.addAttribute("account", acc);
        List<Account> accounts = accountDAO.timkiemacc(keyword);
        int totalAccounts = accounts.size();
        int totalPages = (int) Math.ceil(totalAccounts / (double) pageSize);

        // Lấy danh sách tài khoản trên trang hiện tại
        int start = (page - 1) * pageSize;

        int end = Math.min(start + pageSize, totalAccounts);
        List<Account> accountsOnPage = accounts.subList(start, end);

        // Đưa thông tin về dữ liệu và phân trang vào Model
        AccountPage accountPage = new AccountPage();
        accountPage.setAccounts(accountsOnPage);
        accountPage.setTotalPages(totalPages);
        accountPage.setCurrentPage(page);
        m.addAttribute("accountPage", accountPage);

        return "Admin/qltk";
    }

    @PostMapping("/create")
    public ResponseEntity<String> them(Model m, @RequestParam(value = "username" , defaultValue = "")String username,
                               @RequestParam(value = "password", defaultValue = "")String password,
                               @RequestParam(value = "hoten", defaultValue = "")String hoten,
                               @RequestParam(value = "radio", defaultValue = "")String radio,
                               @RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "") String keyword,
                               @RequestParam("mulimage") MultipartFile file) throws IOException {
        Account acc = accountDAO.laytk(username);
        if (acc != null) {
            List<Account> accounts = accountDAO.timkiemacc(keyword);
            int totalAccounts = accounts.size();
            int totalPages = (int) Math.ceil(totalAccounts / (double) pageSize);
            System.out.println("Tài hoản của bạn đã bị trùng ");
            // Lấy danh sách tài khoản trên trang hiện tại
            int start = (page - 1) * pageSize;
            int end = Math.min(start + pageSize, totalAccounts);
            List<Account> accountsOnPage = accounts.subList(start, end);

            // Đưa thông tin về dữ liệu và phân trang vào Model
            AccountPage accountPage = new AccountPage();
            accountPage.setAccounts(accountsOnPage);
            accountPage.setTotalPages(totalPages);
            accountPage.setCurrentPage(page);
            m.addAttribute("error", "Tài khoản đã tồn tại");
            m.addAttribute("accountPage", accountPage);
            return ResponseEntity.badRequest().body("Tài khoản đã tồn tại !");
        }
        //System.out.println("Username: " + username.length() + " \n password: " + password.length() );
        if(username.trim().length() == 0 || password.trim().length() == 0 || radio.trim().length() == 0 ){
            return ResponseEntity.badRequest().body("Vui lòng nhập đầy đủ thông tin !");
        }
        Account ac = new Account();
        ac.setTps_Username(username);
        ac.setTps_Password(password);
        ac.setTps_Fullname(hoten);
        if(radio.equals("kickhoat")){
            ac.setTps_Active(true);
        }else{
            ac.setTps_Active(false);
        }
        if(file.isEmpty() == true){
            return ResponseEntity.badRequest().body("Ảnh của bạn không được để trống !");
        }
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("folder", "photo"));
        String url = uploadResult.get("url").toString();
        ac.setTps_Photo(url);
        accountDAO.save(ac);
        List<Account> accounts = accountDAO.timkiemacc(keyword);
        int totalAccounts = accounts.size();
        int totalPages = (int) Math.ceil(totalAccounts / (double) pageSize);

        // Lấy danh sách tài khoản trên trang hiện tại
        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, totalAccounts);
        List<Account> accountsOnPage = accounts.subList(start, end);

        // Đưa thông tin về dữ liệu và phân trang vào Model
        AccountPage accountPage = new AccountPage();
        accountPage.setAccounts(accountsOnPage);
        accountPage.setTotalPages(totalPages);
        accountPage.setCurrentPage(page);
        m.addAttribute("accountPage", accountPage);
        m.addAttribute("message","Thêm tài khoản thành công");
        return ResponseEntity.ok("Thêm tài khoản thành công !");



    }

        @PostMapping("/save")
        public String save(Model m,@RequestParam("username")String username,
                           @RequestParam("password")String password,
                           @RequestParam("fullname")String fullname,
                           @RequestParam("radio")String radio,
                           @RequestParam(defaultValue = "1") int page,
                           @RequestParam(defaultValue = "") String keyword
                           ) throws IOException {

            Account ac =accountDAO.laytk(username);
            ac.setTps_Fullname(fullname);
            if(radio.equals("kickhoat")){
                ac.setTps_Active(true);
            }else{
                ac.setTps_Active(false);
            }
            accountDAO.save(ac);


            List<Account> accounts = accountDAO.timkiemacc(keyword);
            int totalAccounts = accounts.size();
            int totalPages = (int) Math.ceil(totalAccounts / (double) pageSize);

            // Lấy danh sách tài khoản trên trang hiện tại
            int start = (page - 1) * pageSize;
            int end = Math.min(start + pageSize, totalAccounts);
            List<Account> accountsOnPage = accounts.subList(start, end);

            // Đưa thông tin về dữ liệu và phân trang vào Model
            AccountPage accountPage = new AccountPage();
            accountPage.setAccounts(accountsOnPage);
            accountPage.setTotalPages(totalPages);
            accountPage.setCurrentPage(page);
            m.addAttribute("accountPage", accountPage);
            return "redirect:/admin/tai-khoan/edit?username="+username;
        }

        @RequestMapping("/reset")
        public String reset(Model m){
        return "redirect:/admin/tai-khoan";
        }

}

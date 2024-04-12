package com.pts.controller.admin;

import com.pts.dao.AccountDAO;
import com.pts.dao.AuthorityDao;
import com.pts.dao.RoleDAO;
import com.pts.entity.AccountPage;
import com.pts.entity.Authority;
import com.pts.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/phan-quyen")
public class phanQuyenController{

    @Autowired
    AuthorityDao authorityDao;
    @Autowired
    AccountDAO accountDAO;
    @Autowired
    RoleDAO roleDAO;
    private final int pageSize = 8;
    @GetMapping
    public String index(Model m,@RequestParam(defaultValue = "1") int page,
                        @RequestParam(defaultValue = "") String keyword){
        List<Role> role =roleDAO.findAll();
        m.addAttribute("roles",role);
        List<Authority>authorities;

        if (keyword.equals("")) {
            authorities =authorityDao.findAll();
        } else {
            System.out.println(keyword);
            authorities = authorityDao.timkiem(keyword);
        }
        m.addAttribute("authorities",authorities);
        int totalAccounts = authorities.size();
        int totalPages = (int) Math.ceil(totalAccounts / (double) pageSize);

        // Lấy danh sách tài khoản trên trang hiện tại
        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, totalAccounts);
        List<Authority> accountsOnPage = authorities.subList(start, end);

        // Đưa thông tin về dữ liệu và phân trang vào Model
        AccountPage accountPage = new AccountPage();
        accountPage.setAuthorities(authorities);
        accountPage.setTotalPages(totalPages);
        accountPage.setCurrentPage(page);
        m.addAttribute("accountPage", accountPage);
        return "Admin/phanQuyen";

    }
    @PostMapping("/set-role")
    @ResponseBody
    public String setRole(@RequestParam("username") String username, @RequestParam("role") String role) {
//        System.out.println(username);
//        System.out.println(role);
        Authority authority = authorityDao.laytk(username);
        Role newRole = roleDAO.getById(role);
        authority.setRole(newRole);
        authorityDao.save(authority);
        return "Success";
    }




}

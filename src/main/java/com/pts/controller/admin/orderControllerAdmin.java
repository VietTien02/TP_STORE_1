package com.pts.controller.admin;

import com.pts.dao.CourseDAO;
import com.pts.dao.OrderDAO;
import com.pts.entity.AccountPage;
import com.pts.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin/order")
public class orderControllerAdmin {
    private final int pageSize = 12;
    @Autowired
    OrderDAO orderDAO;
    @Autowired
    CourseDAO daoc;



    @GetMapping
    public String index(Model m,
                        @RequestParam(defaultValue = "1") int page,
                        @RequestParam(defaultValue = "") String keyword,
                        @RequestParam(defaultValue = "") String startDateStr,
                        @RequestParam(defaultValue = "") String endDateStr) {
        System.out.println(startDateStr);
        List<Order> orderList;
        LocalDate startDate = null;
        LocalDate endDate = null;
        if (keyword.isEmpty() && startDateStr.isEmpty() && endDateStr.isEmpty()) {
            orderList = orderDAO.findAll();
        } else {
            if (!startDateStr.isEmpty()) {
                startDate = LocalDate.parse(startDateStr);
            }
            if (!endDateStr.isEmpty()) {
                endDate = LocalDate.parse(endDateStr);
            }
            Date startDateDate = startDate != null ? Date.valueOf(startDate) : null;
            Date endDateDate = endDate != null ? Date.valueOf(endDate) : null;
            orderList = orderDAO.searchOrders(keyword, startDateDate, endDateDate);

        }

        int totalAccounts = orderList.size();

        int totalPages = (int) Math.ceil(totalAccounts / (double) pageSize);

        // Lấy danh sách tài khoản trên trang hiện tại
        int start = (page - 1) * pageSize;

        int end = Math.min(start + pageSize, totalAccounts);
        List<Order> accountsOnPage = orderList.subList(start, end);

        // Đưa thông tin về dữ liệu và phân trang vào Model
        AccountPage accountPage = new AccountPage();
        accountPage.setOrderList(accountsOnPage);
        accountPage.setTotalPages(totalPages);
        accountPage.setCurrentPage(page);
        m.addAttribute("accountPage", accountPage);

        return "/Admin/order";

    }


}

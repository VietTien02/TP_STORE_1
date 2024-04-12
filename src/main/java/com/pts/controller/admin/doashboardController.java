package com.pts.controller.admin;

import com.pts.dao.AccountDAO;
import com.pts.dao.CourseDAO;
import com.pts.dao.OrderDAO;
import com.pts.dao.PaymentRepository;
import com.pts.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.Year;
import java.util.*;

@Controller
@RequestMapping("/admin/doasboard")
public class doashboardController {

    @Autowired
    AccountDAO accountDAO;
    @Autowired
    OrderDAO orderDAO;
    @Autowired
    CourseDAO courseDAO;
    @Autowired
    PaymentRepository paymentRepository;

    @GetMapping
    public String index(Model m, @RequestParam(defaultValue = "2024")int year) {
        m.addAttribute("year",year);
        Calendar calendar = Calendar.getInstance();
        System.out.println(year);
        List<Integer> monthlyRevenues = new ArrayList<>();
        int totalRevenue = 0;
        LocalDate currentDate = LocalDate.now();

        for (int month = Calendar.JANUARY; month <= Calendar.DECEMBER; month++) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            Date startDate = calendar.getTime();

            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            Date endDate = calendar.getTime();

            List<Order> orders = orderDAO.findOrdersBetweenDates(startDate, endDate);

            int revenue = orders.stream().mapToInt(order -> order.getCourseor().getTps_Price()).sum();
            totalRevenue += revenue;
            monthlyRevenues.add(revenue);
        }
        m.addAttribute("monthlyRevenues",monthlyRevenues);

        //doanh thu trung bình tháng
        double avgRevenue = (double) totalRevenue / 12;
        m.addAttribute("avgRevenue",avgRevenue);

        //Doanh thu trung bình theo ngày
        int daysInYear = Year.of(year).length();
        int avgRevenuePerDay = (int) totalRevenue / daysInYear;
        m.addAttribute("avgRevenuePerDay", avgRevenuePerDay);
        System.out.println(avgRevenuePerDay);

        //tông doanh thu
        double tong = (double) totalRevenue;
        m.addAttribute("tong",tong);

        //Tổng Số Người dùng
        int soluongngdung= (int) accountDAO.count();
        m.addAttribute("soluongngdung",soluongngdung);

        //tông số khóa học đã bán
       Long totalcourse=orderDAO.countOrdersByYear(year);
        m.addAttribute("totalcourse",totalcourse);
        System.out.println(totalcourse);

        // trung bình tháng số lượng khóa học được bán
        long  trungbinh =totalcourse/12;
        m.addAttribute("trungbinh",trungbinh);

        //giao dich thanh cong
        long sumgd=paymentRepository.sumGD();
        m.addAttribute("sumgd",sumgd);
        int soluongcourse =(int) courseDAO.count();
        m.addAttribute("soluongcourse",soluongcourse);



        return "/Admin/doasboard";
    }

}

package com.pts.controller.site;

import com.pts.dao.*;
import com.pts.entity.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

//@RestController
@Controller
@RequestMapping("/return")
public class IPNController {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderDAO orderDAO;
    @Autowired
    private UserContentDAO userContentDAO;
    @Autowired
    AccountDAO accountDAO;
    @Autowired
    CourseDAO courseDAO;


    @GetMapping
    public String showRespond(
            @RequestParam Long vnp_Amount,
            @RequestParam String vnp_BankCode,
            @RequestParam(required = false) String vnp_BankTranNo,
            @RequestParam String vnp_CardType,
            @RequestParam String vnp_OrderInfo,
            @RequestParam String vnp_PayDate,
            @RequestParam String vnp_ResponseCode,
            @RequestParam String vnp_TmnCode,
            @RequestParam String vnp_TransactionNo,
            @RequestParam String vnp_TransactionStatus,
            @RequestParam String vnp_TxnRef,
            @RequestParam String vnp_SecureHash,
            HttpServletResponse resp, Model m, HttpServletRequest request) {

        Payment paymentEntity = paymentRepository.timma(vnp_TxnRef);
        String paymentTxnRef = paymentEntity.getTxnref();
        if(vnp_TransactionStatus.equals("02")){
            m.addAttribute("error","Giao dich không thành công do hủy giao dịch");
            return "redirect:/error-thanh-toan";
        }
        if (!vnp_TxnRef.equals(paymentTxnRef)) {
            m.addAttribute("error","Tham chiếu giao dịch không khớp, nếu có bất kỳ thiếu nại lh Email:crouseonline@gmail.com");
            return "site/error";
        } else if (paymentEntity.getStatus().equals("YES")) {
            m.addAttribute("error","Giao dich Đã được thanh toán vui lòng không lạm dung");
            return "site/error";
        }

        if (vnp_TransactionStatus.equals("00")) {
            TransactionEntity transactionEntity = new TransactionEntity();
            transactionEntity.setTransactionno(vnp_TransactionNo);
            transactionEntity.setTxnref(vnp_TxnRef);
            transactionEntity.setAmount(vnp_Amount/100);
            transactionEntity.setTransactionstatus(vnp_TransactionStatus);
            transactionEntity.setBanktranno(vnp_BankTranNo);
            transactionEntity.setPayment(paymentEntity);
            transactionRepository.save(transactionEntity);
            paymentEntity.setBankcode(vnp_BankCode);
            paymentEntity.setStatus("YES");
            paymentRepository.save(paymentEntity);

            //lưu khóa học vào order
            Order order = new Order();
            order.setCourseor(paymentEntity.getCoursePayment());
            order.setTps_username(paymentEntity.getUsername());
            order.setTps_date(new Date());
            order.setTps_statuscode(false);
            order.setPay("VNPAY");

            orderDAO.save(order);

            //mở khóa học đầu tiên
            Account account = accountDAO.laytk(request.getRemoteUser());
            UserContent userContent = new UserContent();
            userContent.setAccount(account);
            userContent.setCourse(paymentEntity.getCoursePayment());
            userContent.setCompleted(true);
            userContent.setTime("0:0:0");

            int orderInfo = Integer.parseInt(vnp_OrderInfo);
            Course course = courseDAO.findById(orderInfo);
            Content firstContent = null;
            for (Chapter chapter : course.getChapters()) {
                for (Content content : chapter.getContents()) {
                    if (content.getTps_linkytb() != null) {
                        firstContent = content;
                        break;
                    }
                }
                if (firstContent != null) {
                    break;
                }
            }
            if (firstContent != null) {
                userContent.setContent(firstContent);
                userContentDAO.save(userContent);
            } else {
                // handle case when no content found
            }

//            m.addAttribute("error","Cám ơn bạn đã mua khóa học");
//            m.addAttribute("ID",paymentEntity.getCoursePayment());
//            m.addAttribute("firstContent",firstContent);

            return "redirect:/thanh-toan-thanh-cong?courseId=" + paymentEntity.getCoursePayment().getTps_id() +"&ContentId="+firstContent.getTps_id();


        }

        return "redirect:/index";
    }




}
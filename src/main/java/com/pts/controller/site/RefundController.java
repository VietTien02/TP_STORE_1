package com.pts.controller.site;

import com.pts.config.Config;
import com.pts.dao.TransactionRepository;
import com.pts.dto.PymentRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
@RestController
@RequestMapping("/refund")
public class RefundController {
    @Autowired
    TransactionRepository transactionRepository;
    @PostMapping
    public String refund(@ModelAttribute PymentRequestDTO pymentRequestDTO ,HttpServletRequest request, HttpServletResponse resp) throws IOException {

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");


        if(transactionRepository.findByTransactionno(pymentRequestDTO.getVnp_TransactionNo())==null){
            return "Loi";
        }

        pymentRequestDTO=pymentRequestDTO.builder()
                .vnp_RequestId(Config.getRandomNumber(8))
                .vnp_Version("2.1.0")
                .vnp_Command("refund")
                .vnp_TmnCode(Config.vnp_TmnCode)
                .vnp_TxnRef(transactionRepository.findByTransactionno(pymentRequestDTO.getVnp_TransactionNo()).getTxnref())
                .vnp_OrderInfo("Hello")
                .vnp_TransactionNo(pymentRequestDTO.getVnp_TransactionNo())
                .vnp_TransactionDate(formatter.format(cld.getTime()))
                .vnp_createDate(formatter.format(cld.getTime()))
                .vnp_IpAddr(Config.getIpAddress(request))
                .build();

        if(pymentRequestDTO.getVnp_Amount()==null||pymentRequestDTO.getVnp_Amount().trim().equals("")) pymentRequestDTO.setVnp_Amount("0");

        System.out.println("refundRequestDTO: "+pymentRequestDTO.getVnp_TransactionNo());

        pymentRequestDTO.generateHash();
        System.out.println(pymentRequestDTO);

        String paymentUrl = "https://sandbox.vnpayment.vn/merchant_webapi/api/transaction" + "?" + pymentRequestDTO.generateUrl();

        return paymentUrl;

    }
}

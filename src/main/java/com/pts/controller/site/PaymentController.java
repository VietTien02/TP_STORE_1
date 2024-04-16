package com.pts.controller.site;
import com.pts.config.Config;
import com.pts.dao.CourseDAO;
import com.pts.dao.PaymentRepository;
import com.pts.entity.Course;
import com.pts.entity.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    CourseDAO courseDAO;

    @PostMapping
    public String payment(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String vnp_CurrCode="VND";
        String vnp_OrderInfo = req.getParameter("course");
        String orderType = "1";
        String vnp_TxnRef = Config.getRandomNumber(8);
        String vnp_IpAddr = Config.getIpAddress(req);
        String vnp_TmnCode = Config.vnp_TmnCode;
        int amount = Integer.parseInt(req.getParameter("vnp_Amount"))*100;

        int course = Integer.parseInt(req.getParameter("course"));
        Course course1 =courseDAO.findById(course);
        String username= req.getRemoteUser();
        System.out.println(amount);

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
        vnp_Params.put("vnp_OrderType", orderType);
        String bank_code = req.getParameter("bankcode");
        if (bank_code != null && !bank_code.isEmpty()) vnp_Params.put("vnp_BankCode", bank_code);
        vnp_Params.put("vnp_ReturnUrl", Config.vnp_Returnurl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        String locate = req.getParameter("language");
        if (locate != null && !locate.isEmpty()) vnp_Params.put("vnp_Locale", locate);
        else vnp_Params.put("vnp_Locale", "vn");

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());

        Payment paymentEntity=new Payment();
        paymentEntity.setAmount(amount/100);
        paymentEntity.setStatus("NO");
        paymentEntity.setTxnref(vnp_TxnRef);
        paymentEntity.setCreatedate(vnp_CreateDate);
//        paymentEntity.setBankcode(bank_code);
        paymentEntity.setCurrcode(vnp_CurrCode);
        paymentEntity.setCoursePayment(course1);
        paymentEntity.setUsername(username);
        paymentRepository.save(paymentEntity);


        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String hr =Config.vnp_HashSecret;
        System.out.println(hr);
        String vnp_SecureHash = Config.hmacSHA512(Config.vnp_HashSecret, hashData.toString());
        System.out.println(vnp_SecureHash);
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = Config.vnp_PayUrl + "?" + queryUrl;
        resp.setStatus(HttpServletResponse.SC_FOUND);
        resp.setHeader("Location", paymentUrl);

        return "redirect:" + paymentUrl;
    }



}

package com.pts.controller.admin;

import com.pts.dao.PaymentRepository;
import com.pts.entity.AccountPage;
import com.pts.entity.Payment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/admin/payment")
public class PayMentController {
    private final int pageSize = 12;
    @Autowired
    PaymentRepository paymentRepository;
    @GetMapping
    public String index(Model m,@RequestParam(defaultValue = "1") int page,
                        @RequestParam(defaultValue = "") String keyword){

        List<Payment> paymentList;
        if (keyword == "") {
            paymentList =paymentRepository .findAll();
            m.addAttribute("paymentList", paymentList);

        }
        else {
            paymentList = paymentRepository.timtaikhoan(keyword);
        }

        int totalAccounts = paymentList.size();

        int totalPages = (int) Math.ceil(totalAccounts / (double) pageSize);

        // Lấy danh sách tài khoản trên trang hiện tại
        int start = (page - 1) * pageSize;

        int end = Math.min(start + pageSize, totalAccounts);
        List<Payment> accountsOnPage = paymentList.subList(start, end);

        // Đưa thông tin về dữ liệu và phân trang vào Model
        AccountPage accountPage = new AccountPage();
        accountPage.setPaymentList(accountsOnPage);
        accountPage.setTotalPages(totalPages);
        accountPage.setCurrentPage(page);
        m.addAttribute("accountPage", accountPage);

        return "/Admin/Payment";
    }
    @PostMapping("/exportToExcel")
    public ResponseEntity<byte[]> exportToExcel(@RequestParam(required = false) String keyword) throws IOException {
        List<Payment> paymentList;
        if (keyword == null || keyword.isEmpty()) {
            paymentList = paymentRepository.findAll();
        } else {
            paymentList = paymentRepository.timtaikhoan(keyword);
        }

        // tạo workbook và sheet
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Payments");

        // tạo header
        XSSFRow header = sheet.createRow(0);
        header.createCell(0).setCellValue("STT");
        header.createCell(1).setCellValue("Ngân Hàng");
        header.createCell(2).setCellValue("Số Tiền");
        header.createCell(3).setCellValue("Nội dung thanh toán");
        header.createCell(4).setCellValue("Người Mua");
        header.createCell(5).setCellValue("Trạng Thái");
        header.createCell(6).setCellValue("Ngày tạo");
        sheet.setColumnWidth(0, 5000);
        sheet.setColumnWidth(1, 5000);
        sheet.setColumnWidth(2, 5000);
        sheet.setColumnWidth(3, 15000);
        sheet.setColumnWidth(4, 10000);
        sheet.setColumnWidth(5, 8000);
        sheet.setColumnWidth(6, 8000);

        // lặp qua danh sách tài khoản và tạo các row tương ứng
        int rowNum = 1;
        for (Payment payment : paymentList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(payment.getId());
            row.createCell(1).setCellValue(payment.getBankcode());
            row.createCell(2).setCellValue(payment.getAmount());
            row.createCell(3).setCellValue(payment.getCoursePayment().getTps_Name());
            row.createCell(4).setCellValue(payment.getUsername());
            row.createCell(5).setCellValue(payment.getStatus().equals("YES") ? "Thành công" : "Thất bại");
            LocalDateTime createdate = LocalDateTime.parse(payment.getCreatedate(),
                    DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            row.createCell(6).setCellValue(createdate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        }

        // tạo byte array từ workbook
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        workbook.write(byteArrayOutputStream);
        workbook.close();
        byte[] bytes = byteArrayOutputStream.toByteArray();

        // thiết lập header và type cho response
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "payments.xlsx");

        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }

}

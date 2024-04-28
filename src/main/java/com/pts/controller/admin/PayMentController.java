package com.pts.controller.admin;

import com.pts.dao.PaymentRepository;
import com.pts.entity.AccountPage;
import com.pts.entity.Payment;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
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
import java.util.concurrent.atomic.AtomicInteger;

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

        // Tạo một CellStyle mới để định dạng header cells
        CellStyle headerCellStyle = workbook.createCellStyle();

// Đặt font đậm và căn giữa
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerCellStyle.setFont(headerFont);
        headerCellStyle.setAlignment(HorizontalAlignment.CENTER); // căn giữa

// Đặt màu nền xanh dương
        headerCellStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerFont.setColor(IndexedColors.WHITE.getIndex());

// Tạo border cho header cells
        headerCellStyle.setBorderTop(BorderStyle.THIN);
        headerCellStyle.setBorderBottom(BorderStyle.THIN);
        headerCellStyle.setBorderLeft(BorderStyle.THIN);
        headerCellStyle.setBorderRight(BorderStyle.THIN);

        // Tạo một Font mới và đặt font chữ là Times New Roman và kích thước là 20
        Font font = workbook.createFont();
        font.setFontName("Times New Roman");
        font.setFontHeightInPoints((short) 20);

// Tạo CellStyle mới để định dạng ô gộp
        CellStyle mergedCellStyle = workbook.createCellStyle();
        mergedCellStyle.setAlignment(HorizontalAlignment.CENTER); // căn giữa

// Đặt font cho CellStyle
        mergedCellStyle.setFont(font);

// Tạo dòng mới để chứa ô gộp
        Row headerHistory = sheet.createRow(0);

// Gộp các ô thành một
        sheet.addMergedRegion(new CellRangeAddress(
                0, // Từ hàng 0
                0, // Đến hàng 0 (chỉ một dòng)
                0, // Từ cột 0
                6  // Đến cột 6
        ));

// Tạo ô mới trong dòng và thiết lập nội dung "Lịch sử thanh toán"
        Cell mergedCell = headerHistory.createCell(0); // Tạo ô mới
        mergedCell.setCellValue("Lịch sử thanh toán");

// Thiết lập CellStyle cho ô gộp
        mergedCell.setCellStyle(mergedCellStyle);
        Row rowTime = sheet.createRow(2);
        CellStyle mergedCellStyleTime = workbook.createCellStyle();
        mergedCellStyleTime.setAlignment(HorizontalAlignment.CENTER);
// Gộp các ô trong dòng thứ 3
        sheet.addMergedRegion(new CellRangeAddress(
                2, // Từ hàng 2
                2, // Đến hàng 2 (chỉ một dòng)
                0, // Từ cột 0
                6  // Đến cột 6
        ));

// Tạo ô mới trong dòng và thiết lập nội dung là thời gian hiện tại
        Cell mergedCellRow3 = rowTime.createCell(0); // Tạo ô mới
        mergedCellRow3.setCellValue("Ngày tao: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        mergedCellRow3.setCellStyle(mergedCellStyleTime);
// Tạo và thiết lập header cells
        XSSFRow header = sheet.createRow(4);
        header.createCell(0).setCellValue("STT");
        header.createCell(1).setCellValue("Ngân Hàng");
        header.createCell(2).setCellValue("Số Tiền");
        header.createCell(3).setCellValue("Nội dung thanh toán");
        header.createCell(4).setCellValue("Người Mua");
        header.createCell(5).setCellValue("Trạng Thái");
        header.createCell(6).setCellValue("Ngày tạo");

        header.setHeightInPoints(30);

// Áp dụng CellStyle đã tạo cho từng header cell
        for (int i = 0; i < 7; i++) {
            Cell cell = header.getCell(i);
            cell.setCellStyle(headerCellStyle);
        }

// Thiết lập chiều rộng cho các cột
        sheet.setColumnWidth(0, 1500);
        sheet.setColumnWidth(1, 4000);
        sheet.setColumnWidth(2, 4000);
        sheet.setColumnWidth(3, 10000);
        sheet.setColumnWidth(4, 7000);
        sheet.setColumnWidth(5, 6000);
        sheet.setColumnWidth(6, 6000);

// Tạo một Font mới và đặt kích thước font là 13
        Font cellFont = workbook.createFont();
        cellFont.setFontHeightInPoints((short) 13);

// Tạo một CellStyle mới để định dạng các cell trong bảng dữ liệu
        CellStyle cellStyle = workbook.createCellStyle();

// Đặt font cho cellStyle
        cellStyle.setFont(cellFont);

// Tạo border cho cell
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);

// Lặp qua danh sách tài khoản và tạo các row tương ứng
        int rowNum = 5;
        AtomicInteger index = new AtomicInteger(1);
        for (Payment payment : paymentList) {
            Row row = sheet.createRow(rowNum++);
            for (int i = 0; i < 7; i++) {
                Cell cell = row.createCell(i);
                if (i == 0) {
                    cell.setCellValue(index.getAndIncrement());
                } else if (i == 1) {
                    cell.setCellValue(payment.getBankcode());
                } else if (i == 2) {
                    cell.setCellValue(payment.getAmount());
                } else if (i == 3) {
                    cell.setCellValue(payment.getCoursePayment().getTps_Name());
                } else if (i == 4) {
                    cell.setCellValue(payment.getUsername());
                } else if (i == 5) {
                    cell.setCellValue(payment.getStatus().equals("YES") ? "Thành công" : "Thất bại");
                } else if (i == 6) {
                    LocalDateTime createdate = LocalDateTime.parse(payment.getCreatedate(), DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
                    cell.setCellValue(createdate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
                }
                cell.setCellStyle(cellStyle);
            }
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

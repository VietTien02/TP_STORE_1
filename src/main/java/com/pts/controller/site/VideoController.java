package com.pts.controller.site;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class VideoController {
    @Autowired
    private Cloudinary cloudinary;

    @PostMapping("/uploadVideo")
    public ResponseEntity<String> uploadVideo(@RequestParam("file") MultipartFile file) throws IOException {
        Map<String, Object> uploadParams = new HashMap<>();
        String uniqueFileName = file.getOriginalFilename()+ "-" +UUID.randomUUID().toString()  ;
        uploadParams.put("folder", "samples");
        uploadParams.put("public_id", uniqueFileName); // Đặt tên cho tệp tin
        uploadParams.put("resource_type", "video");
        uploadParams.put("chunk_size", 100000000); // Kích thước chunk tối đa
        uploadParams.put("timeout", 120000); // Thời gian tối đa để tải lên

//        uploadParams.put("use_filename", true); // Sử dụng tên tệp tin ban đầu
//        uploadParams.put("unique_filename", false); // Không tạo tên tệp tin duy nhất
//        uploadParams.put("overwrite", true); // Ghi đè lên tệp tin đã có nếu có

        Map<String, Object> result = cloudinary.uploader().upload(file.getBytes(), uploadParams);

        String url = result.get("url").toString();
        return new ResponseEntity<>(url, HttpStatus.OK);
    }

}

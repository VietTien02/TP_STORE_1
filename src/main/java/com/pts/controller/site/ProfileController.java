package com.pts.controller.site;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.pts.dao.AccountDAO;
import com.pts.entity.Account;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
@Controller
public class ProfileController {
    @Autowired
    AccountDAO accountDAO;
    @Autowired
    private Cloudinary cloudinary;

    /*
    * Đây là mô hình 3 tier n đi từ tầng controler
    *
    *
    *  "/site/profile" đoạn này nó sẽ trả ra front-end và hiển thị front lên giao diện
    *
    *
    *
    *
    *
    * */
    @GetMapping("/profile")
    public String profile(HttpServletRequest request, Model m){
        String us=request.getRemoteUser();
        Account acc =accountDAO.laytk(us);
        m.addAttribute("acc",acc);
        return "/site/profile";
    }
    @PostMapping("/edit-name")
    public String editname(Model m,@RequestParam("fullname")String fullname,HttpServletRequest request) {
    	String username=request.getRemoteUser();
        Account acc =accountDAO.laytk(username);
        acc.setTps_Fullname(fullname);
        accountDAO.save(acc);
    	return "redirect:/profile";
    }
    
    //sulyanh
    @PostMapping("/edit-photo")
    public String editphoto(Model m,@RequestParam("mulimage") MultipartFile file
            ,HttpServletRequest request)throws IOException{
        String username=request.getRemoteUser();
        Account acc =accountDAO.laytk(username);
//    	String uniqueFileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
//		File filePath = new File("src/main/webapp/image");
//		file.transferTo(new File(filePath.getAbsolutePath() + "/" + uniqueFileName));
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("folder", "photo"));
        String url = uploadResult.get("url").toString();

        acc.setTps_Photo(url);
        accountDAO.save(acc);
        return "redirect:/profile";
    }
}

package com.pts.controller.site;

import com.pts.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SmsController {
    @Autowired
    private SmsService smsService;

    @RequestMapping("/send-sms")
    public void sendSms() {
        smsService.sendSms("+84815767495", "em chào anh Tiến");
    }
}

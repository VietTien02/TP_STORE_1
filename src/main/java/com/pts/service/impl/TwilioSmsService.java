package com.pts.service.impl;

import com.pts.service.SmsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
public class TwilioSmsService implements SmsService {
    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Override
    public void sendSms(String to, String message) {
        Twilio.init(accountSid, authToken);

        Message.creator(new PhoneNumber(to), new PhoneNumber("+13203919380"), message).create();
    }
}

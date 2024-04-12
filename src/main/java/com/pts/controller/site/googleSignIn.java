package com.pts.controller.site;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.net.URLEncoder;

@Controller
public class googleSignIn {
    @Autowired
    private Environment env;


    @GetMapping("/auth/signin/google")
    public String googleSignIn() {
        String clientId = env.getProperty("spring.security.oauth2.client.registration.google.client-id");
        String redirectUri = URLEncoder.encode(env.getProperty("spring.security.oauth2.client.registration.google.redirect-uri"));
        String scope = env.getProperty("spring.security.oauth2.client.registration.google.scope");
        String url = "https://accounts.google.com/o/oauth2/v2/auth?client_id=" + clientId + "&response_type=code&scope=" + scope + "&redirect_uri=" + redirectUri;
        return "redirect:" + url;
    }
}

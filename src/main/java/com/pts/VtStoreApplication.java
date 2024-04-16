package com.pts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@Configuration
@EnableAsync(proxyTargetClass = true)
@EnableCaching(proxyTargetClass = true)
//    @ComponentScan({"com.pts.Controller.site","com.pts.Controller.admin"
//            ,"com.pts.ServiceImpl"
//            ,"com.pts.Service"
//            ,"com.pts.help","com.pts.DAO"
//            ,"com.pts.entity"
//            ,"com.pts.help",
//            "com.pts.Config","com.pts.DTO"})
public class VtStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(VtStoreApplication.class, args);
    }

}

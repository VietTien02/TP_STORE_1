    package com.pts.config;
    import org.springframework.core.env.Environment;
    import org.springframework.http.HttpHeaders;
    import com.pts.service.UserService;
    import com.pts.service.impl.AccountServiceImpl;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.core.annotation.Order;
    import org.springframework.http.HttpEntity;
    import org.springframework.http.HttpMethod;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
    import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
    import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
    import org.springframework.security.oauth2.core.user.OAuth2User;
    import org.springframework.web.client.RestTemplate;

    import java.util.Map;


    @Configuration
    @EnableWebSecurity
    @Order(1)
    public class AuthConfig extends WebSecurityConfigurerAdapter {
        @Autowired
        AccountServiceImpl accountService;

        @Autowired
        UserService userService;
        @Autowired
        BCryptPasswordEncoder pe;
        @Autowired
        private Environment env;


        @Bean
        public BCryptPasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder() {
                @Override
                public boolean matches(CharSequence rawPassword, String encodedPassword) {
                    // Mật khẩu không cần mã hóa
                    return rawPassword.toString().equals(encodedPassword);
                }
            };
        }
        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception{
            auth.userDetailsService(userService);
        }
        /*--phan quyen su dung va hinh thuc dang nhap--*/
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            // CDRP, CORS
            http.csrf().disable().cors().disable();

            // phan quyen su dung
            http.authorizeRequests()
    		.antMatchers("/admin/chuong","/admin/danh-muc","/admin/doasboard","/admin/khoa-hoc","/admin/noi-dung","/admin/order","/admin/payment","/admin/phan-quyen","/admin/code-khoahoc","/admin/quiz","/admin/quiz-add","/admin/tai-khoan").hasRole("Admin")
    //		.antMatchers("/home/users").hasAnyRole("ADMIN","USER")
    		.antMatchers("/profile","/add-course/**","/my-courses","/checkout/**","/change-password","/quiz","/learning/**/**").authenticated()
                    .anyRequest().permitAll();// anonymous

            // dieu khien loi truy cap khong dung vai tro
            http.exceptionHandling().accessDeniedPage("/auth/signin");// [/error]
//â
            // giao dien dang nhap
            http.formLogin().loginPage("/auth/signin")
                    .loginProcessingUrl("/auth/signin")// [/login]
                    .defaultSuccessUrl("/", false).failureUrl("/login/error")
                    .usernameParameter("username") // [username]
                    .passwordParameter("password");// [password]
            http.rememberMe().rememberMeParameter("remember").tokenValiditySeconds(86400); // [remember-me]
//            http.oauth2Login()
//                    .loginPage("/login")
//                    .userInfoEndpoint()
//                    .userService(oAuth2UserService())
//                    .and()
//                    .defaultSuccessUrl("/", false)
//                    .failureUrl("/login?error=true");

            // dang xuat
            http.logout().logoutUrl("/auth/logoff")// [/logout]
                    .logoutSuccessUrl("/auth/signin");//chuyen trang

        }
        @Bean
        public OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService() {
            DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
            return userRequest -> {
                OAuth2User user = delegate.loadUser(userRequest);
                // Lấy access token để gọi API Google
                String accessToken = userRequest.getAccessToken().getTokenValue();
                // Lấy thông tin người dùng từ Google API
                String userInfoUri = env.getProperty("spring.security.oauth2.client.provider.google.user-info-uri");
                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                headers.setBearerAuth(accessToken);
                HttpEntity<String> entity = new HttpEntity<>("", headers);
                ResponseEntity<Map> response = restTemplate.exchange(userInfoUri, HttpMethod.GET, entity, Map.class);
                Map<String, Object> userInfo = response.getBody();
                // Xử lý thông tin người dùng từ Google API
                // ...
                return user;
            };
        }


    }

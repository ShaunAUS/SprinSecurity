package io.security.basicSecurity;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

public class BasicSecurityController {

    @GetMapping("/")
    public String index() {
        return "home";
    }

    @GetMapping("loginPage")
    public String loginPage() {
        return "loginPage";
    }

    @GetMapping("/admin/pay")
    public String adminPay() {
        return "adminPay";


    }

    @GetMapping("/admin/**")
    public String admin() {
        return "admin";

    }

    @GetMapping("/login")
    public String login() {
        return "login";

    }
    @GetMapping("/denined")
    public String denined() {
        return "denined";

    }

    @GetMapping("/")
    public String indexx(HttpSession session) {

        //인증객체를 불러올수 있다. contextHolder = static
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        //세션에 시큐리티 컨텍스트가 저장되기 떄문에 세션으로도 인증유저를 불러올수있다.
        //세션키값으로 저장된 securityContext  불러오기
        SecurityContext context = (SecurityContext)session.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
        Authentication authentication1 = context.getAuthentication();


        return "home";

    }

    @GetMapping("/thread")
    public String thread() {

        new Thread(

                new Runnable() {
                    @Override
                    public void run() {
                        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                    }
                }
        ).start();
        return "thread";

    }
}
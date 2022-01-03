package io.security.basicSecurity;

import org.springframework.web.bind.annotation.GetMapping;

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
}
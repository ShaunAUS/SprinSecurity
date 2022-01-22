package corespringsecurity.controller.login;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class loginController {


 @GetMapping("/login")
 public String login(){

  return"user/login/login";
 }

 @GetMapping("/logout")
 public String logout(HttpServletRequest request, HttpServletResponse response){

  //로그인 했으면 securityContext 안에 인증에 성공한 authentication 객체가있다
  Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

  if(authentication != null){
   //logout filter 에서사용
    new SecurityContextLogoutHandler().logout(request,response,authentication);
  }

  return"redirect:/login";
 }

}

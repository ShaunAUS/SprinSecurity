package io.security.basicSecurity;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected  void configure(HttpSecurity http) throws Exception{

        //인가 정책
        http

                .authorizeRequests()
                .anyRequest().authenticated(); // 어떠한 요청에도 보안'인증'이 필요하다.

        //인증 정챍
        http
                .formLogin()
                .loginPage("/loginPage") // 로그인 페이지
                .defaultSuccessUrl("/") //로그인 성공시
                .failureUrl("/loginPage")//로그인 실패시
                .usernameParameter("userId")
                .passwordParameter("passWd")
                .loginProcessingUrl("login_proc")
                //로그인 성공시
                //인증한 결과를 담는다
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        System.out.println("authentication = " + authentication.getName());  //인증에 성공한 유저 네임
                        response.sendRedirect("/"); //인증 성공뒤 루트페이지로
                    }
                })
                //실패했을경우 이 handler 호출
                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                        System.out.println("exception = " + exception.getMessage());
                        response.sendRedirect("/loginPage");
                    }
                })
                .permitAll();


    }
}

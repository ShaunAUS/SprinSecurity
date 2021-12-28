package io.security.basicSecurity;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected  void configure(HttpSecurity http) throws Exception{

        //인가 정책
        http

                .authorizeRequests()
                .anyRequest().authenticated(); // 어떠한 요청에도 보안인증이 필요하다.

        //인증 정챍
        http
                .formLogin();
    }
}

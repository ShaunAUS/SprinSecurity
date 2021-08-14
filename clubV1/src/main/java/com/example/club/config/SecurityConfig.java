package com.example.club.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Log4j2
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    //configure() 파라미터가 다른 여러 메서드가 있다
    
    
    //접근 제한// 인가(허가) 설정
    @Override
    protected void configure(HttpSecurity http)throws Exception{


        //authorizeRequests() 인증이 필요한 자원(페이지) 선택
        //antMatchers() 앤스스타일의 패턴으로 원하는 자원 선택(페이지)
        //permitAll() 말그대로 누구에게나 허용(로그인 필요 x)
        http.authorizeRequests()
                .antMatchers("/sample/all").permitAll()

                //지정한 자원(페이지)에는 권한(role) 이 user인 접속자만 접속 허용 한다.
                //USER 이라는 단어는  ROLE_USER 라는 '상수'와 같은 의미이다.
                //스프링 부트에서는 USER라는 단어를 상수처럼 인증된 사용자를 의미한다.
                .antMatchers("/sample/member").hasRole("USER");

                //인가/인증 절차에 문제 생겼을때 로그인 페이지를 보여주도록 화면으로 이동
                http.formLogin();
                //csrf 비활성화
                http.csrf().disable();
                http.logout();
    }
    
    
    @Override
    //암호화된 패스워드 사용자 만들기
    protected void configure(AuthenticationManagerBuilder auth)throws Exception{
        
        //아이디 설정
        auth.inMemoryAuthentication().withUser("user1")
                
                //암호화된 비밀번호 (7942)
                .password("$2a$10$vG25R4NGP7YQCjdP5XhcdOhDC552utKo3OSjukkRvzfWLd1enSuui")
                //권한 부여
                .roles("USER");
    }

    @Bean
    //PasswordEncoder 는 인터페이스며 구현하던가 구현한 클래스를 이용해 사용 // 스프링 시큐리티에서는 BCryptPasswordEncoder클래스 제공
    //비밀번호 암호화
    PasswordEncoder passwordEncoder(){

        //BCryptPasswordEncoder 는 bcrypt 라는 해시 함수를 이용해서 패스워드를 암호화하는 목적으로 설계된 클래스이다
        //BCryptPasswordEncoder 로 암호화된 패스워드는 다시 원래대로 복호화가 불가능 하고 매번 암호값 도다르다
        // 대신에 암호화된 결과와 원본패스워드랑 비교결과 확인 가능 but 원본 보기는 불가.
        // @Bean을 이용해 BCryptPasswordEncoder 지정한다
        return new BCryptPasswordEncoder();
    };
}

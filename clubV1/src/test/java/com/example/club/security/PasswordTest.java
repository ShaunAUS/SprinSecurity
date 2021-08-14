package com.example.club.security;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class PasswordTest {



    @Autowired
    private PasswordEncoder passwordEncoder;
    @Test
    public void testEncode(){

        String password="7942";

        //패스워드 암호화
        String enPw=passwordEncoder.encode(password);
        //암호화된 비밀번호 출력
        System.out.println("enPw"+enPw);
        //암호회된 비밀번호와  원본 비교
        boolean matchResult=passwordEncoder.matches(password,enPw);

        System.out.println("matchResult:"+ matchResult);
    }

}

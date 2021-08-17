package com.example.club.security;


import com.example.club.entity.ClubMember;
import com.example.club.entity.ClubMemberRole;
import com.example.club.repository.ClubMemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class ClubMemberTests {

    @Autowired
    private ClubMemberRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void insertDummies(){


        //기본정보 설정
        IntStream.rangeClosed(1,100).forEach(i->{
            ClubMember clubMember=ClubMember.builder()
            .email("user"+i+"@zerock.org")
            .name("사용자님"+i)
            .fromSocial(false)
            .password( passwordEncoder.encode("1111"))
            .build();


            //권한부여
            //디폴트 role
            //열거형.상수이름
            clubMember.addMemberRole(ClubMemberRole.USER);

            if(i>80){
                clubMember.addMemberRole(ClubMemberRole.MANAGER);
            }
            if(i>90){
                clubMember.addMemberRole(ClubMemberRole.ADMIN);
            }

            repository.save(clubMember);
        });
    }

    @Test
    public void testRead(){

        Optional<ClubMember> result=repository.findByEmail("user95@zerock.org",false);

        ClubMember clubMember=result.get();

        System.out.println(clubMember);
    }
}

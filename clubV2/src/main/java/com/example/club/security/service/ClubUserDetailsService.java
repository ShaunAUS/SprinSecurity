package com.example.club.security.service;


import com.example.club.entity.ClubMember;
import com.example.club.repository.ClubMemberRepository;
import com.example.club.security.dto.ClubAuthMemberDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;


// service 와 repository 연동
@Log4j2
@Service
@RequiredArgsConstructor
public class ClubUserDetailsService implements UserDetailsService {

    private final ClubMemberRepository clubMemberRepository;

    //UserDetailsService 메서드는 이거 하나뿐이다.
    //username =구분자  =email의미한다
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("ClubUserDetailsService loadUserByUsername"+username);

        Optional<ClubMember> result= clubMemberRepository.findByEmail(username,false);

        if(result.isPresent()){
            throw new UsernameNotFoundException("check email or Social");
        }
        //username= email 로 찾은 결과
        ClubMember clubMember= result.get();

        log.info("---------------");
        log.info(clubMember);
        
        
        //clubMember 을 UserDetails 타입으로 처기하리기위해 DTO로 변환
        ClubAuthMemberDTO clubAuthMember = new ClubAuthMemberDTO(
                clubMember.getEmail(),
                clubMember.getPassword(),
                clubMember.isFromSocial(),
                clubMember.getRoleSet().stream().map(role->new SimpleGrantedAuthority("ROLE_"+role.name()))
                .collect(Collectors.toSet())
        );

        clubAuthMember.setName(clubMember.getName());
        clubAuthMember.setFromSocial(clubMember.isFromSocial());


        return clubAuthMember;
    }
}

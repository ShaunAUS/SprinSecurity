package com.example.club.repository;

import com.example.club.entity.ClubMember;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ClubMemberRepository extends JpaRepository<ClubMember,String> {

    //회원의 이메일과 소셜추가된 회원여부에 따라 검색
    //@EntityGraph = 1.LEFT OUTER JOIN 사용/2.연관된 엔티티들을 SQL 한번에 조회하는 방법

    @EntityGraph(attributePaths = {"roleSet"},type=EntityGraph.EntityGraphType.LOAD)
    @Query("select m from ClubMember m where m.formSocial = :social and m.email=:email")
    Optional<ClubMember> findByEmail(String email, boolean social);


}

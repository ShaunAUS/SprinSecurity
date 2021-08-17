package com.example.club.entity;


import lombok.*;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString

public class ClubMember extends BaseEntity{

        @Id
        private String email;

        private String password;

        private String name;

        private boolean fromSocial;
        
        
        //권한설정
        //clubMember는 ClubMemberRole 타입값을 처리하기 위해서 Set~추가
        //ClubMember은 여러권한을 가질수 있다. ClubMember 객체의 일부로만 사용되기 떄문에 @ElementCollection 사용
        @ElementCollection(fetch = FetchType.LAZY)
        @Builder.Default
        private Set<ClubMemberRole> roleSet=new HashSet<>();

        public void addMemberRole(ClubMemberRole clubMemberRole){
                roleSet.add(clubMemberRole);
        }
}

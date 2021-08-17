package com.example.club.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;



@MappedSuperclass //이클래스는 테이블로 생성되지 않는다  // 그래서 @builder 애노 필요없다
@EntityListeners(value={AuditingEntityListener.class}) //엔티티 객체에 어떤변화가 있는지 감지하는 리스너
//리스너를 통해 regDate, modDate 적절한 값이 지정됀다
@Getter
public abstract class BaseEntity {

    @CreatedDate //JPA에서 엔티티 생성시간 // 속성= INSERTABLE, UPDATEABLE 이있다.
    @Column(name="regdate",updatable=false)
    private LocalDateTime regDate;

    @LastModifiedDate//최종 수정시간을 자동으로 처리
    @Column(name="moddate")
    private LocalDateTime modDate;


}

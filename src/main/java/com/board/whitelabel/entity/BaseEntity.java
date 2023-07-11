package com.board.whitelabel.entity;

import lombok.Data;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/*
 * Entity : Spring 에서 테이블의 정보를 1 : 1로 가지고 있는 영속 객체입니다.
 * 이 entity 를 이용해서 테이블 생성은 물론 update insert delete 도 할 수 있습니다.
 * 물론 엔티티를 이용해서 DB에 in-out 을 하는 주체는 repository입니다
 * 
 *  이중 @MappedSuperClass라는 어노테이션의 역할을 볼 겁니다
 *  이 어노테이션이 붙은 클래스는 기본적으로 테이블을 직접 생성하지 않고
 *  얘를 상속받은 클래스가 그 일을 해줍니다
 *  
 *  @아래 메인에 리스너가 등록되기 위해서 EntityListener를 등록해야 합니다(자신의 클래스에)
 *  또한 이 클래스가 활성화 되기 위해서는 main클래스에 @AuthingListener를 적용해줘야 합니다
 *  
 *  그 후 위 @Mapped.. 선언된 클래스를 상속받은 엔티티가 테이블과 1:1 매핑이 되고
 *  repository를 이용해서 테이블 생성 및 데이터를 In-Out 시키는 순서입니다
 *  
 *  그래서 @mAPPER 선언된 클래스는 상속 목적인 추상을 선언되는게 일반적입니다
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Data
@Getter
abstract class BaseEntity {
   
   //컬럼을 생성합니다
   //여기에 생성되는 컬럼은 시간 값을 관리하는 컬럼이고
   //속성을 이용해서 자동으로 값을 업데이트 할 지 여부를 조정할 수 있습니다.
   @CreatedDate
   @Column(name = "regDate", updatable = false)
   private String regDate;
   
   //위의 컬럼은 방명록 작성시간을 나타내는 컬럼이고
   //하위는 수정 시간을 나타내는 컬럼을 정의합니다
   //참고로 수정시간을 자동으로 생성해주는 어노테이션이 있습니다
   @LastModifiedDate
   @Column(name = "modDate")
   private String modDate;

   @PrePersist
   public void onPrePersist() {
      this.regDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
      this.modDate = this.regDate;
   }

   @PreUpdate
   public void onPreUpdate() {
      this.modDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
   }
   
}
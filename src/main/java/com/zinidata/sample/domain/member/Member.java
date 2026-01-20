package com.zinidata.sample.domain.member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Convert;
import com.zinidata.sample.common.converter.EncryptedStringConverter;
import java.time.LocalDateTime;

@Entity
@Table(name = "TB_MEMBER")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Member {

    @Id
    @Column(name = "MEMBER_ID", length = 50, nullable = false)
    private String memberId;

    @Column(name = "PASSWORD", length = 255, nullable = false)
    private String password;

    @Column(name = "NAME", length = 100, nullable = false)
    @Convert(converter = EncryptedStringConverter.class)
    private String name;

    @Column(name = "BIRTH_DATE", length = 100)
    @Convert(converter = EncryptedStringConverter.class)
    private String birthDate;

    /**
     * 성별 (M:남성, F:여성)
     */
    @Column(name = "GENDER", length = 1)
    private String gender;

    @Column(name = "ROLE", length = 20)
    private String role;

    @Column(name = "USE_YN", length = 1)
    private String useYn;

    @CreatedDate
    @Column(name = "REG_DT", updatable = false)
    private LocalDateTime regDt;

    @LastModifiedDate
    @Column(name = "CHG_DT")
    private LocalDateTime chgDt;

    @Builder
    public Member(String memberId, String password, String name, String birthDate, String gender, String role,
            String useYn) {
        this.memberId = memberId;
        this.password = password;
        this.name = name;
        this.birthDate = birthDate;
        this.gender = gender;
        this.role = role != null ? role : "ROLE_USER";
        this.useYn = useYn != null ? useYn : "Y";
    }
}

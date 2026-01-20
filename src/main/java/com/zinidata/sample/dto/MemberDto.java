package com.zinidata.sample.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDto {
    private String memberId;
    private String password;
    private String name;
    private String birthDate;
    private String gender;
}

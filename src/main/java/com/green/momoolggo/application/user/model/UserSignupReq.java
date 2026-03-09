package com.green.momoolggo.application.user.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSignupReq {
    private String name;
    private String userId;
    private String userPw;
    private Integer gender;
    private String birth;
    private String tel;
    private String role;
}
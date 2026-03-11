package com.green.momoolggo.application.user.model;

import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
public class UserGetRes {
    private String userId;
    private String name;
    private String tel;
    private int    gender;
    private String birth;
}
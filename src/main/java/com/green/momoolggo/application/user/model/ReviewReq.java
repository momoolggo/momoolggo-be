package com.green.momoolggo.application.user.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewReq {
    private long orderId;
    private long userNo;
    private String text;
    private String image;
    private int rating;
}

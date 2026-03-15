package com.green.momoolggo.application.store.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FavoriteToggleReq {
    private long userNo;
    private long storeId;
}

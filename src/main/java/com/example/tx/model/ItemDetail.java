package com.example.tx.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ItemDetail {
    private Long itemDetailId;
    private Long itemId;
    private String size;
    private Long stock;
}


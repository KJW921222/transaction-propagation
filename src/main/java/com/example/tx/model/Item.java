package com.example.tx.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter
@Builder
public class Item {
    private Long itemId;
    private String name;
    private BigDecimal price;
    private List<ItemDetail> itemDetails;
}

package com.example.tx.repository;

import com.example.tx.model.Item;
import com.example.tx.model.ItemDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TestRepository {
    void addItem(Item item);
    void addItemDetail(@Param("itemId") Long itemId, @Param("itemDetails") List<ItemDetail> itemDetails);
    int countAllItems();
    int countAllItemDetails();
}

package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderHasItem {
    private Integer id;
    private String orderId;
    private String productId;
    private int qty;
    private double amount;
}

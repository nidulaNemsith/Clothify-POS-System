package org.example.dto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductOnCart {
    private String productID;
    private String productName;
    private Integer qty;
    private Double amount;
}

package com.sparta.msa_exam.order.orders;

import lombok.*;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long productId;
    private String name;
    private Integer supplyPrice;
}

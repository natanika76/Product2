package ru.natali.orders.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter @Setter
public class OrderRequest {
    private String productArticle;
    private int quantity;
    private BigDecimal totalAmount;
}

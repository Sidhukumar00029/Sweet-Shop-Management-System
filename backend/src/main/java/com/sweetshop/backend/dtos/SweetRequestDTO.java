package com.sweetshop.backend.dtos;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class SweetRequestDTO {
    private String name;
    private String category;
    private BigDecimal price;
    private int quantity;
}

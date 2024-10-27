package ru.skillbox.orderservice.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductPrice {

    @Schema(description = "product id")
    Integer id;
    @Schema(description = "product price")
    Double price;
}

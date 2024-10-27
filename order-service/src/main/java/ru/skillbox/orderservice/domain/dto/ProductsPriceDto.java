package ru.skillbox.orderservice.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ProductsPriceDto {

    public ProductsPriceDto() {

    }

    @Schema(description = "list of products with price")
    List<ProductPrice> productPriceList;
}

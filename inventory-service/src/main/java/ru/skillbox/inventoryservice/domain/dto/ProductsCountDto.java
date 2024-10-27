package ru.skillbox.inventoryservice.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ProductsCountDto {

    public ProductsCountDto() {

    }

    @Schema(description = "list of products with count")
    List<ProductCount> productCountList;
}

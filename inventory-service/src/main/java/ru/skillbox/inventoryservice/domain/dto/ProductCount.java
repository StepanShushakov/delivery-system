package ru.skillbox.inventoryservice.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductCount {

    @Schema(description = "product id")
    Integer id;
    @Schema(description = "product count")
    Double count;
}

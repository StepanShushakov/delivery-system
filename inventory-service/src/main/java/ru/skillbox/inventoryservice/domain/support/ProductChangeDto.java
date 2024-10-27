package ru.skillbox.inventoryservice.domain.support;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.skillbox.inventoryservice.domain.Product;

@Data
@AllArgsConstructor
public class ProductChangeDto {
    private Product product;
    private double count;
}

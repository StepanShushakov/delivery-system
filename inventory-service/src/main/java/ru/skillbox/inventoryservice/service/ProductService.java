package ru.skillbox.inventoryservice.service;

import ru.skillbox.inventoryservice.domain.dto.ProductsCountDto;

public interface ProductService {
    void addProductsCount(ProductsCountDto productsCountDto);
}

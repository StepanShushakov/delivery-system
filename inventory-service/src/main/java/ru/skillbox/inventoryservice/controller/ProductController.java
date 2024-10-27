package ru.skillbox.inventoryservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.inventoryservice.domain.dto.ProductsCountDto;
import ru.skillbox.inventoryservice.service.ProductService;

@RestController
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "adding product count")
    @PostMapping("/add")
    public String addProductsCount(@RequestBody ProductsCountDto productsCountDto) {
        productService.addProductsCount(productsCountDto);
        return "добавлены продукты с указанным количеством";
    }

}

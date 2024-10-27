package ru.skillbox.inventoryservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.skillbox.inventoryservice.domain.Product;
import ru.skillbox.inventoryservice.domain.dto.ProductCount;
import ru.skillbox.inventoryservice.domain.dto.ProductsCountDto;
import ru.skillbox.inventoryservice.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void addProductsCount(ProductsCountDto productsCountDto) {
        List<Product> productList = new ArrayList<>();
        for (ProductCount productCount : productsCountDto.getProductCountList()) {
            Optional<Product> optProduct = productRepository.findById(productCount.getId());
            if (optProduct.isPresent()) {
                Product product = optProduct.get();
                product.setCount(productCount.getCount() + product.getCount());
                productList.add(product);
            } else {
                Product product = new Product();
                product.setId(productCount.getId());
                product.setCount(productCount.getCount());
                productList.add(product);
            }
        }
        productRepository.saveAll(productList);
    }
}

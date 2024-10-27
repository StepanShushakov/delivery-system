package ru.skillbox.orderservice.service;

import ru.skillbox.orderservice.domain.Order;
import ru.skillbox.orderservice.domain.dto.OrderDto;
import ru.skillbox.orderservice.domain.dto.ProductsPriceDto;

import java.util.Optional;

public interface OrderService {

    Optional<Order> addOrder(OrderDto orderDto);

    void setProductsPrice(ProductsPriceDto productsPriceDto);
}

package ru.skillbox.paymentservice.domain.events;

import lombok.Data;
import ru.skillbox.paymentservice.domain.dto.ProductDto;

import java.util.ArrayList;
import java.util.List;

@Data
public class OrderCreatedEvent implements Event {

    private static final String EVENT = "OrderPurchase";

    private Long orderId;
    private Long userId;
    private double price;
    List<ProductDto> products = new ArrayList<>();

    public Event setOrderId(Long orderId) {
        this.orderId = orderId;
        return this;
    }

    public Event setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public Event setPrice(double price) {
        this.price = price;
        return this;
    }

    public Event setProducts(List<ProductDto> products) {
        this.products = products;
        return this;
    }

    @Override
    public String getEvent() {
        return EVENT;
    }
}

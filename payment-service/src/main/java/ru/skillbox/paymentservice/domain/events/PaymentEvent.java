package ru.skillbox.paymentservice.domain.events;

import lombok.Getter;
import lombok.ToString;
import ru.skillbox.paymentservice.domain.dto.ProductDto;
import ru.skillbox.paymentservice.domain.enums.PaymentStatus;

import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
public class PaymentEvent implements Event {

    private static final String EVENT = "Payment";

    private Long orderId;
    private PaymentStatus status;
    private double price;
    List<ProductDto> products = new ArrayList<>();

    public PaymentEvent() {
    }

    public PaymentEvent orderId(Long orderId) {
        this.orderId = orderId;
        return this;
    }

    public PaymentEvent status(PaymentStatus status) {
        this.status = status;
        return this;
    }

    public PaymentEvent price(double price) {
        this.price = price;
        return this;
    }

    public PaymentEvent products(List<ProductDto> products) {
        this.products = products;
        return this;
    }

    @Override
    public String getEvent() {
        return EVENT;
    }

}

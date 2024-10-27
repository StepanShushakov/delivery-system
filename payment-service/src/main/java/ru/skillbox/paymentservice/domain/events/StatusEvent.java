package ru.skillbox.paymentservice.domain.events;

import lombok.Getter;
import lombok.ToString;
import ru.skillbox.paymentservice.domain.dto.ProductDto;
import ru.skillbox.paymentservice.domain.dto.StatusDto;

import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
public class StatusEvent implements Event {

    private static final String EVENT = "PaymentStatusStatus";

    private Long orderId;
    private StatusDto statusDto;
    List<ProductDto> products = new ArrayList<>();

    public StatusEvent() {
    }

    public StatusEvent orderId(Long orderId) {
        this.orderId = orderId;
        return this;
    }

    public void setStatusDto(StatusDto statusDto) {
        this.statusDto = statusDto;
    }

    public StatusEvent products(List<ProductDto> products) {
        this.products = products;
        return this;
    }

    @Override
    public String getEvent() {
        return EVENT;
    }

}

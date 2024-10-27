package ru.skillbox.orderservice.domain.events;

import lombok.Getter;
import lombok.ToString;
import ru.skillbox.orderservice.domain.dto.StatusDto;

@ToString
@Getter
public class StatusEvent implements Event {
    private static final String EVENT = "OrderStatusEvent";

    private Long orderId;
    private StatusDto statusDto;

    public StatusEvent orderId(Long orderId) {
        this.orderId = orderId;
        return this;
    }

    @Override
    public String getEvent() {
        return EVENT;
    }

}

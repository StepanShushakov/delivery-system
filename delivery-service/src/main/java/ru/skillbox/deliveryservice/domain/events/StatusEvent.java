package ru.skillbox.deliveryservice.domain.events;

import lombok.Getter;
import lombok.ToString;
import ru.skillbox.deliveryservice.domain.dto.StatusDto;

@ToString
@Getter
public class StatusEvent implements Event {

    private static final String EVENT = "DeliveryStatusEvent";

    private Long orderId;
    private StatusDto statusDto;

    public StatusEvent() {
    }

    public StatusEvent orderId(Long orderId) {
        this.orderId = orderId;
        return this;
    }

    public void setStatusDto(StatusDto statusDto) {
        this.statusDto = statusDto;
    }

    @Override
    public String getEvent() {
        return EVENT;
    }

}

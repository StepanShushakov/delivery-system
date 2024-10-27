package ru.skillbox.deliveryservice.domain.events;

import lombok.Getter;
import lombok.ToString;
import ru.skillbox.deliveryservice.domain.enums.DeliveryStatus;

@ToString
@Getter
public class DeliveryEvent implements Event {

    private static final String EVENT = "Inventory";

    private Long orderId;
    private DeliveryStatus status;

    public DeliveryEvent() {
    }

    public DeliveryEvent orderId(Long orderId) {
        this.orderId = orderId;
        return this;
    }

    public DeliveryEvent status(DeliveryStatus status) {
        this.status = status;
        return this;
    }

    @Override
    public String getEvent() {
        return EVENT;
    }

}

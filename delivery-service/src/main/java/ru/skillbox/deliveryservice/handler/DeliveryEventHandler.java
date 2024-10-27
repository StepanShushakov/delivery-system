package ru.skillbox.deliveryservice.handler;

import org.springframework.stereotype.Component;
import ru.skillbox.deliveryservice.domain.dto.StatusDto;
import ru.skillbox.deliveryservice.domain.enums.OrderStatus;
import ru.skillbox.deliveryservice.domain.enums.ServiceName;
import ru.skillbox.deliveryservice.domain.events.DeliveryEvent;
import ru.skillbox.deliveryservice.domain.events.StatusEvent;

import javax.transaction.Transactional;

import static ru.skillbox.deliveryservice.domain.enums.DeliveryStatus.*;


@Component
public class DeliveryEventHandler implements EventHandler<DeliveryEvent, StatusEvent> {

    @Transactional
    public StatusEvent handleEvent(DeliveryEvent event) {

        StatusEvent statusEvent = new StatusEvent()
                .orderId(event.getOrderId());
        StatusDto statusDto = new StatusDto();
        statusDto.setServiceName(ServiceName.DELIVERY_SERVICE);
        if (DELIVERED.equals(event.getStatus())) {
            statusDto.setStatus(OrderStatus.DELIVERED);
            statusDto.setComment("Order delivered");
        } else if (DELIVERY_FAIL.equals(event.getStatus())) {
            statusDto.setStatus(OrderStatus.DELIVERY_FAILED);
            statusDto.setComment("Product could not be delivered");
        }
        statusEvent.setStatusDto(statusDto);
        return statusEvent;
    }
}

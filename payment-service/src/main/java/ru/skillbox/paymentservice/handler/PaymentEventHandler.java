package ru.skillbox.paymentservice.handler;

import org.springframework.stereotype.Component;
import ru.skillbox.paymentservice.domain.dto.StatusDto;
import ru.skillbox.paymentservice.domain.enums.OrderStatus;
import ru.skillbox.paymentservice.domain.enums.ServiceName;
import ru.skillbox.paymentservice.domain.events.PaymentEvent;
import ru.skillbox.paymentservice.domain.events.StatusEvent;

import javax.transaction.Transactional;

import static ru.skillbox.paymentservice.domain.enums.PaymentStatus.APPROVED;
import static ru.skillbox.paymentservice.domain.enums.PaymentStatus.DECLINED;

@Component
public class PaymentEventHandler implements EventHandler<PaymentEvent, StatusEvent> {

    @Transactional
    public StatusEvent handleEvent(PaymentEvent event) {

        StatusEvent statusEvent =  new StatusEvent()
                .orderId(event.getOrderId())
                .products(event.getProducts());
        StatusDto statusDto = new StatusDto();
        statusDto.setServiceName(ServiceName.PAYMENT_SERVICE);
        if (APPROVED.equals(event.getStatus())) {
            statusDto.setStatus(OrderStatus.PAID);
            statusDto.setComment("Order payment");
        } else if (DECLINED.equals(event.getStatus())) {
            statusDto.setStatus(OrderStatus.PAYMENT_FAILED);
            statusDto.setComment("Insufficient funds");
        }
        statusEvent.setStatusDto(statusDto);
        return statusEvent;
    }
}

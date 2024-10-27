package ru.skillbox.orderservice.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.skillbox.orderservice.domain.Order;
import ru.skillbox.orderservice.domain.dto.StatusDto;
import ru.skillbox.orderservice.domain.events.StatusEvent;
import ru.skillbox.orderservice.repository.OrderRepository;

@Component
@Slf4j
public class StatusEventConsumer implements EventConsumer<StatusEvent> {

    private final OrderRepository orderRepository;

    @Autowired
    public StatusEventConsumer(
            OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void consumeEvent(StatusEvent event) {
        orderRepository.findById(event.getOrderId())
                .ifPresent(order -> {
                    updateOrderStatus(order, event.getStatusDto());
                    orderRepository.save(order);
                });
    }

    public void updateOrderStatus(Order order, StatusDto statusDto) {
        if (order.getStatus() == statusDto.getStatus()) {
            log.info("Request with same status {} for order {} from service {}", statusDto.getStatus(), order.getId(), statusDto.getServiceName());
            return;
        }
        order.setStatus(statusDto.getStatus());
        order.addStatusHistory(statusDto.getStatus(), statusDto.getServiceName(), statusDto.getComment());
    }

}

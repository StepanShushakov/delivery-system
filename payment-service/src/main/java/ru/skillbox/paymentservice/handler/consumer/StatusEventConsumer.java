package ru.skillbox.paymentservice.handler.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.skillbox.paymentservice.domain.Order;
import ru.skillbox.paymentservice.domain.User;
import ru.skillbox.paymentservice.domain.dto.StatusDto;
import ru.skillbox.paymentservice.domain.events.StatusEvent;
import ru.skillbox.paymentservice.repository.OrderRepository;
import ru.skillbox.paymentservice.repository.UserRepository;

import javax.transaction.Transactional;

import static ru.skillbox.paymentservice.domain.enums.OrderStatus.INVENTMENT_FAILED;
import static ru.skillbox.paymentservice.domain.enums.OrderStatus.DELIVERY_FAILED;
import static ru.skillbox.paymentservice.domain.enums.ServiceName.INVENTORY_SERVICE;
import static ru.skillbox.paymentservice.domain.enums.ServiceName.DELIVERY_SERVICE;

@Component
@Slf4j
public class StatusEventConsumer implements EventConsumer<StatusEvent> {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Autowired
    public StatusEventConsumer(OrderRepository orderRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void consumeEvent(StatusEvent event) {
        StatusDto statusDto = event.getStatusDto();
        if ((statusDto.getServiceName() == INVENTORY_SERVICE
                && statusDto.getStatus() == INVENTMENT_FAILED)
            || (statusDto.getServiceName() == DELIVERY_SERVICE
                && statusDto.getStatus() == DELIVERY_FAILED)) {
            rollbackPayment(event.getOrderId());
        }
    }

    private void rollbackPayment(Long orderId) {
        Order order = orderRepository.getReferenceById(orderId);
        User user = order.getUser();
        user.setBalance(user.getBalance() + order.getCost());
        userRepository.save(user);
    }
}

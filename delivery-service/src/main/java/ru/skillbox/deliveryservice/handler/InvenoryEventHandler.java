package ru.skillbox.deliveryservice.handler;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.skillbox.deliveryservice.domain.Order;
import ru.skillbox.deliveryservice.domain.dto.StatusDto;
import ru.skillbox.deliveryservice.domain.events.DeliveryEvent;
import ru.skillbox.deliveryservice.domain.events.StatusEvent;
import ru.skillbox.deliveryservice.repository.OrderRepository;

import javax.transaction.Transactional;

import java.util.Optional;
import java.util.Random;

import static ru.skillbox.deliveryservice.domain.enums.DeliveryStatus.*;
import static ru.skillbox.deliveryservice.domain.enums.OrderStatus.INVENTED;
import static ru.skillbox.deliveryservice.domain.enums.ServiceName.INVENTORY_SERVICE;

@Component
public class InvenoryEventHandler implements EventHandler<StatusEvent, DeliveryEvent> {

    private final OrderRepository orderRepository;
    private final Logger logger = LoggerFactory.getLogger(InvenoryEventHandler.class);

    @Autowired
    public InvenoryEventHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public DeliveryEvent handleEvent(@NotNull StatusEvent event) {
        StatusDto statusDto = event.getStatusDto();
        if (statusDto.getServiceName() != INVENTORY_SERVICE) {
            return null;
        }
        if (statusDto.getStatus() != INVENTED) {
            return null;
        }
        Order order = getOrder(event.getOrderId());
        DeliveryEvent deliveryEvent = new DeliveryEvent()
                .orderId(event.getOrderId())
                .status(DELIVERY_FAIL);
        try {
            if (deliveryIsComplete(order)) {
                deliveryEvent.status(DELIVERED);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return deliveryEvent;
    }

    private boolean deliveryIsComplete(Order order) throws InterruptedException {
        boolean result = (new Random().nextInt(100) + 1) > 15;
        long latency = 60000 + new Random().nextLong(60000);
        Thread.sleep(latency);
        logger.info("доставка заказа заняла {} сек. и завершилась {}", latency / 1000, result ? "успешно" : "неудачей");
        order.setStatus(result ? DELIVERED : DELIVERY_FAIL);
        orderRepository.save(order);
        return result;
    }

    private Order getOrder(Long orderId) {
        Optional<Order> optOrder = orderRepository.findById(orderId);
        if (optOrder.isPresent()) {
            return optOrder.get();
        }
        Order newOrder = new Order();
        newOrder.setId(orderId);
        return newOrder;
    }

}

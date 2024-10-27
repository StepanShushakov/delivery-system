package ru.skillbox.orderservice.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Sinks;
import ru.skillbox.orderservice.domain.Order;
import ru.skillbox.orderservice.domain.dto.ProductDto;
import ru.skillbox.orderservice.domain.events.OrderCreatedEvent;
import java.util.List;

@Component
public class OrderProcessor {

    private final Sinks.Many<OrderCreatedEvent> sink;
    private final Logger logger = LoggerFactory.getLogger(OrderProcessor.class);

    @Autowired
    public OrderProcessor(Sinks.Many<OrderCreatedEvent> sink) {
        this.sink = sink;
    }


    public void process(Order order, List<ProductDto> products){

        long orderId = order.getId();
        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent();
        orderCreatedEvent.setOrderId(orderId);
        orderCreatedEvent.setPrice(order.getCost());
        orderCreatedEvent.setUserId(order.getUserId());
        orderCreatedEvent.setProducts(products);
        sink.emitNext(orderCreatedEvent, Sinks.EmitFailureHandler.FAIL_FAST);

        logger.info(String.format("Sent to Kafka -> %s", orderCreatedEvent));

    }
}

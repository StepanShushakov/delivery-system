package ru.skillbox.paymentservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.skillbox.paymentservice.domain.events.*;
import ru.skillbox.paymentservice.handler.EventHandler;
import ru.skillbox.paymentservice.handler.consumer.EventConsumer;

import java.util.function.Consumer;
import java.util.function.Function;

@Configuration
public class PaymentServiceConfig {

    private final EventHandler<PaymentEvent, StatusEvent> paymentEventHandler;
    private final EventHandler<OrderCreatedEvent, PaymentEvent> orderCreatedEventHandler;
    private final EventConsumer<StatusEvent> statusEventConsumer;

    @Autowired
    public PaymentServiceConfig(
            EventHandler<PaymentEvent, StatusEvent> paymentEventHandler,
            EventHandler<OrderCreatedEvent, PaymentEvent> orderCreatedEventHandler, EventConsumer<StatusEvent> statusEventConsumer) {
        this.paymentEventHandler = paymentEventHandler;
        this.orderCreatedEventHandler = orderCreatedEventHandler;
        this.statusEventConsumer = statusEventConsumer;
    }

    @Bean
    public Function<OrderCreatedEvent, PaymentEvent> orderCreatedEventProcessor() {
        return orderCreatedEventHandler::handleEvent;
    }

    @Bean
    public Function<PaymentEvent, StatusEvent> paymentEventSubscriber() {
        return paymentEventHandler::handleEvent;
    }

    @Bean
    public Consumer<StatusEvent> statusEventProcessor() {
        return statusEventConsumer::consumeEvent;
    }
}

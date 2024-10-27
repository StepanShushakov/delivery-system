package ru.skillbox.deliveryservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.skillbox.deliveryservice.domain.events.*;
import ru.skillbox.deliveryservice.handler.EventHandler;

import java.util.function.Function;

@Configuration
public class DeliveryServiceConfig {

    private final EventHandler<DeliveryEvent, StatusEvent> deliveryEventHandler;
    private final EventHandler<StatusEvent, DeliveryEvent> inventoryEventHandler;

    @Autowired
    public DeliveryServiceConfig(
            EventHandler<DeliveryEvent, StatusEvent> deliveryEventHandler,
            EventHandler<StatusEvent, DeliveryEvent> inventoryEventHandler) {
        this.deliveryEventHandler = deliveryEventHandler;
        this.inventoryEventHandler = inventoryEventHandler;
    }

    @Bean
    public Function<StatusEvent, DeliveryEvent> inventoryEventProcessor() {
        return inventoryEventHandler::handleEvent;
    }

    @Bean
    public Function<DeliveryEvent, StatusEvent> deliveryEventSubscriber() {
        return deliveryEventHandler::handleEvent;
    }

}

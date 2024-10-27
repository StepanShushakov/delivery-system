package ru.skillbox.inventoryservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.skillbox.inventoryservice.domain.events.*;
import ru.skillbox.inventoryservice.handler.EventHandler;
import ru.skillbox.inventoryservice.handler.consumer.EventConsumer;

import java.util.function.Consumer;
import java.util.function.Function;

@Configuration
public class InventoryServiceConfig {

    private final EventHandler<InventoryEvent, StatusEvent> inventoryEventHandler;
    private final EventHandler<StatusEvent, InventoryEvent> paymentEventHandler;
    private final EventConsumer<StatusEvent> statusEventConsumer;

    @Autowired
    public InventoryServiceConfig(
            EventHandler<InventoryEvent, StatusEvent> inventoryEventHandler,
            EventHandler<StatusEvent, InventoryEvent> paymentEventHandler, EventConsumer<StatusEvent> statusEventConsumer) {
        this.inventoryEventHandler = inventoryEventHandler;
        this.paymentEventHandler = paymentEventHandler;
        this.statusEventConsumer = statusEventConsumer;
    }

    @Bean
    public Function<StatusEvent, InventoryEvent> paymentEventProcessor() {
        return paymentEventHandler::handleEvent;
    }

    @Bean
    public Function<InventoryEvent, StatusEvent> inventoryEventSubscriber() {
        return inventoryEventHandler::handleEvent;
    }

    @Bean
    public Consumer<StatusEvent> statusEventProcessor() {
        return statusEventConsumer::consumeEvent;
    }

}

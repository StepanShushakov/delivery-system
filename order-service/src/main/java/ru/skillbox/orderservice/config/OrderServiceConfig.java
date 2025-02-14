package ru.skillbox.orderservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import ru.skillbox.orderservice.consumer.EventConsumer;
import ru.skillbox.orderservice.domain.events.StatusEvent;
import ru.skillbox.orderservice.domain.events.OrderCreatedEvent;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Configuration
public class OrderServiceConfig {

    private final EventConsumer<StatusEvent> statusEventConsumer;

    @Autowired
    public OrderServiceConfig(EventConsumer<StatusEvent> statusEventConsumer) {
        this.statusEventConsumer = statusEventConsumer;
    }

    @Bean
    public Sinks.Many<OrderCreatedEvent> sink() {
        return Sinks.many()
                .multicast()
                .directBestEffort();
    }

    @Bean
    public Supplier<Flux<OrderCreatedEvent>> orderEventPublisher(
            Sinks.Many<OrderCreatedEvent> publisher) {
        return publisher::asFlux;
    }

    @Bean
    public Consumer<StatusEvent> statusEventProcessor() {
        return statusEventConsumer::consumeEvent;
    }

}

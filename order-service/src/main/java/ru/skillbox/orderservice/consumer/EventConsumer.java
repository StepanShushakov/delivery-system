package ru.skillbox.orderservice.consumer;

import ru.skillbox.orderservice.domain.events.Event;

public interface EventConsumer<T extends Event> {

    void consumeEvent(T event);
}

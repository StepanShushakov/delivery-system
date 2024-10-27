package ru.skillbox.inventoryservice.handler.consumer;

import ru.skillbox.inventoryservice.domain.events.Event;

public interface EventConsumer<T extends Event> {

    void consumeEvent(T event);

}

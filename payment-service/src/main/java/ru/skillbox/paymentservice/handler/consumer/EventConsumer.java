package ru.skillbox.paymentservice.handler.consumer;

import ru.skillbox.paymentservice.domain.events.Event;

public interface EventConsumer<T extends Event> {

    void consumeEvent(T event);

}

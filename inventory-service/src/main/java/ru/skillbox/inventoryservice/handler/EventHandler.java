package ru.skillbox.inventoryservice.handler;

import ru.skillbox.inventoryservice.domain.events.Event;

public interface EventHandler<T extends Event, R extends Event> {

    R handleEvent(T event);
}

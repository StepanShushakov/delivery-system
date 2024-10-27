package ru.skillbox.inventoryservice.handler;

import org.springframework.stereotype.Component;
import ru.skillbox.inventoryservice.domain.dto.StatusDto;
import ru.skillbox.inventoryservice.domain.enums.OrderStatus;
import ru.skillbox.inventoryservice.domain.enums.ServiceName;
import ru.skillbox.inventoryservice.domain.events.InventoryEvent;
import ru.skillbox.inventoryservice.domain.events.StatusEvent;

import javax.transaction.Transactional;

import static ru.skillbox.inventoryservice.domain.enums.InventoryStatus.FULLED;
import static ru.skillbox.inventoryservice.domain.enums.InventoryStatus.INCOMPLETE;


@Component
public class InventoryEventHandler implements EventHandler<InventoryEvent, StatusEvent> {

    @Transactional
    public StatusEvent handleEvent(InventoryEvent event) {

        StatusEvent statusEvent = new StatusEvent()
                .orderId(event.getOrderId())
                .products(event.getProducts());
        StatusDto statusDto = new StatusDto();
        statusDto.setServiceName(ServiceName.INVENTORY_SERVICE);
        if (FULLED.equals(event.getStatus())) {
            statusDto.setStatus(OrderStatus.INVENTED);
            statusDto.setComment("Order reserved");
        } else if (INCOMPLETE.equals(event.getStatus())) {
            statusDto.setStatus(OrderStatus.INVENTMENT_FAILED);
            statusDto.setComment("Ordered products out of stock");
        }
        statusEvent.setStatusDto(statusDto);
        return statusEvent;
    }
}

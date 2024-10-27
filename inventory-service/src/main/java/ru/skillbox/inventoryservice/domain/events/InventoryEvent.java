package ru.skillbox.inventoryservice.domain.events;

import lombok.Getter;
import lombok.ToString;
import ru.skillbox.inventoryservice.domain.enums.InventoryStatus;
import ru.skillbox.inventoryservice.domain.dto.ProductDto;

import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
public class InventoryEvent implements Event {

    private static final String EVENT = "Inventory";

    private Long orderId;
    private InventoryStatus status;
    List<ProductDto> products = new ArrayList<>();

    public InventoryEvent() {
    }

    public InventoryEvent orderId(Long orderId) {
        this.orderId = orderId;
        return this;
    }

    public InventoryEvent status(InventoryStatus status) {
        this.status = status;
        return this;
    }

    public InventoryEvent products(List<ProductDto> products) {
        this.products = products;
        return this;
    }

    @Override
    public String getEvent() {
        return EVENT;
    }

}

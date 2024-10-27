package ru.skillbox.deliveryservice.domain;

import lombok.Data;
import ru.skillbox.deliveryservice.domain.enums.DeliveryStatus;

import javax.persistence.*;

@Entity
@Data
@Table(name = "orders")
public class Order {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    public Order() {

    }
}

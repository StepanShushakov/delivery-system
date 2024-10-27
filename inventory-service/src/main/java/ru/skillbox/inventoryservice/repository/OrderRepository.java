package ru.skillbox.inventoryservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skillbox.inventoryservice.domain.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}

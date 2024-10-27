package ru.skillbox.deliveryservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skillbox.deliveryservice.domain.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}

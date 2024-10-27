package ru.skillbox.paymentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skillbox.paymentservice.domain.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}

package ru.skillbox.inventoryservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.inventoryservice.domain.links.Order2Product;

import java.util.List;

@Repository
public interface Order2ProductRepository extends JpaRepository<Order2Product, Long> {

    List<Order2Product> findOrder2ProductByOrderId(Long orderId);
}

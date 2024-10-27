package ru.skillbox.inventoryservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.skillbox.inventoryservice.domain.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query(nativeQuery = true, value = """
select
    p.*
from order2product o2p
    join products p
    on o2p.product_id = p.id
where o2p.order_id = :orderId
""")
    List<Product> findProductsByOrderId(@Param("orderId") long orderId);
}

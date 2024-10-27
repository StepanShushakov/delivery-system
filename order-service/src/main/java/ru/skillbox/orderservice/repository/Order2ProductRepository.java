package ru.skillbox.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.skillbox.orderservice.domain.links.Order2Product;

import java.lang.annotation.Native;

@Repository
public interface Order2ProductRepository extends JpaRepository<Order2Product, Long> {

    @Query (nativeQuery = true, value = """
                                            select
                                            *
                                            from order2product
                                            where order_id = :order_id
                                            and product_id = :product_id
                                            limit 1
                                            """)
    Order2Product findOrder2ProductByOrderIdAndProductId(@Param("order_id") Long orderId,
                                                         @Param("product_id") Integer productId);
}

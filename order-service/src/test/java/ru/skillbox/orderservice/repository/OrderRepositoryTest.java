package ru.skillbox.orderservice.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import ru.skillbox.orderservice.domain.Order;
import ru.skillbox.orderservice.domain.enums.OrderStatus;
import ru.skillbox.orderservice.domain.enums.ServiceName;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
public class OrderRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OrderRepository orderRepositoryJpa;

    @Test
    public void whenGetByDescription_thenReturnOrder() {
        Order order = new Order(
                "Moscow, st.Taganskaya 150",
                "Moscow, st.Tulskaya 24",
                "Order #112",
                1L,
                OrderStatus.REGISTERED
        );
        order.addStatusHistory(OrderStatus.REGISTERED, ServiceName.ORDER_SERVICE, "Order created");
        entityManager.persist(order);
        entityManager.flush();

        String desc = order.getDescription();
        Order gotOrder = orderRepositoryJpa.findByDescription(desc).get();

        assertThat(gotOrder.getDepartureAddress())
                .isEqualTo(order.getDepartureAddress());
        assertThat(gotOrder.getStatus())
                .isEqualTo(order.getStatus());
        assertThat(gotOrder.getOrderStatusHistory().size())
                .isEqualTo(1);

        // test order status change
        gotOrder.setStatus(OrderStatus.PAID);
        gotOrder.addStatusHistory(OrderStatus.PAID, ServiceName.ORDER_SERVICE, "Order paid");
        orderRepositoryJpa.save(gotOrder);

        gotOrder = orderRepositoryJpa.findByDescription(desc).get();
        assertThat(gotOrder.getStatus())
                .isEqualTo(OrderStatus.PAID);
        assertThat(orderRepositoryJpa.findOrderStatusHistoryById(gotOrder.getId()).size())
                .isEqualTo(2);
    }
}

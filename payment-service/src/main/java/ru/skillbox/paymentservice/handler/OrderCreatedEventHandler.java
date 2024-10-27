package ru.skillbox.paymentservice.handler;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.skillbox.paymentservice.domain.Order;
import ru.skillbox.paymentservice.domain.events.OrderCreatedEvent;
import ru.skillbox.paymentservice.domain.events.PaymentEvent;
import ru.skillbox.paymentservice.domain.User;
import ru.skillbox.paymentservice.repository.OrderRepository;
import ru.skillbox.paymentservice.repository.UserRepository;

import javax.transaction.Transactional;

import java.util.Random;

import static ru.skillbox.paymentservice.domain.enums.PaymentStatus.APPROVED;
import static ru.skillbox.paymentservice.domain.enums.PaymentStatus.DECLINED;

@Component
@Slf4j
public class OrderCreatedEventHandler implements EventHandler<OrderCreatedEvent, PaymentEvent> {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private static final Logger logger = LoggerFactory.getLogger(OrderCreatedEventHandler.class);

    @Autowired
    public OrderCreatedEventHandler(UserRepository userRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public PaymentEvent handleEvent(OrderCreatedEvent event) {
        double orderPrice = event.getPrice();
        Long userId = event.getUserId();
        PaymentEvent paymentEvent = new PaymentEvent()
                .orderId(event.getOrderId())
                .price(event.getPrice())
                .products(event.getProducts())
                .status(DECLINED);
        userRepository
                .findById(userId)
                .ifPresent(user -> {
                    try {
                        deductUserBalance(orderPrice, paymentEvent, user);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
        return paymentEvent;
    }

    private void deductUserBalance(double orderPrice, PaymentEvent paymentEvent, User user) throws InterruptedException {
        double userBalance = user.getBalance();
        if (userBalance >= orderPrice) {
            long latency = 2000 + new Random().nextLong(5000);
            logger.info("задержка времени оплаты заказа составила: {} сек.", latency / 1000 );
            Thread.sleep(latency);
            user.setBalance(userBalance - orderPrice);
            userRepository.save(user);
            Order order = new Order(paymentEvent.getOrderId(), orderPrice, user);
            orderRepository.save(order);
            paymentEvent.status(APPROVED);
        }
    }

}

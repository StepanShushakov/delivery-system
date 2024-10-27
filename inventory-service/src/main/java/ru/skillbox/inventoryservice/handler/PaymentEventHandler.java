package ru.skillbox.inventoryservice.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.skillbox.inventoryservice.domain.Order;
import ru.skillbox.inventoryservice.domain.Product;
import ru.skillbox.inventoryservice.domain.links.Order2Product;
import ru.skillbox.inventoryservice.domain.support.ProductChangeDto;
import ru.skillbox.inventoryservice.domain.dto.ProductDto;
import ru.skillbox.inventoryservice.domain.dto.StatusDto;
import ru.skillbox.inventoryservice.domain.events.StatusEvent;
import ru.skillbox.inventoryservice.domain.events.InventoryEvent;
import ru.skillbox.inventoryservice.repository.Order2ProductRepository;
import ru.skillbox.inventoryservice.repository.OrderRepository;
import ru.skillbox.inventoryservice.repository.ProductRepository;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static ru.skillbox.inventoryservice.domain.enums.InventoryStatus.*;
import static ru.skillbox.inventoryservice.domain.enums.OrderStatus.PAID;
import static ru.skillbox.inventoryservice.domain.enums.ServiceName.*;

@Component
public class PaymentEventHandler implements EventHandler<StatusEvent, InventoryEvent> {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final Order2ProductRepository order2ProductRepository;
    private final Logger logger = LoggerFactory.getLogger(PaymentEventHandler.class);

    @Autowired
    public PaymentEventHandler(ProductRepository productRepository, OrderRepository orderRepository, Order2ProductRepository order2ProductRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.order2ProductRepository = order2ProductRepository;
    }

    @Transactional
    public InventoryEvent handleEvent(StatusEvent event) {
        StatusDto statusDto = event.getStatusDto();
        if (statusDto.getServiceName() != PAYMENT_SERVICE) {
            return null;
        }
        if (statusDto.getStatus() != PAID) {
            return null;
        }
        Order order = getOrder(event.getOrderId());
        InventoryEvent inventoryEvent = new InventoryEvent()
                .orderId(event.getOrderId())
                .products(event.getProducts())
                .status(INCOMPLETE);
        try {
            if (checkStock(order, event.getProducts())) {
                inventoryEvent.status(FULLED);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return inventoryEvent;
    }

    private Order getOrder(Long orderId) {
        Optional<Order> optOrder = orderRepository.findById(orderId);
        if (optOrder.isPresent()) {
            return optOrder.get();
        }
        Order newOrder = new Order();
        newOrder.setId(orderId);
        return newOrder;
    }

    private synchronized boolean checkStock(Order order, List<ProductDto> products) throws InterruptedException {
        List<ProductChangeDto> productChangeList = new ArrayList<>();
        for (ProductDto productDto: products) {
            Optional<Product> optProduct = productRepository.findById(productDto.getProductId());
            if (!checkProductCount(optProduct, productDto, productChangeList)) {
                returnCounts(productChangeList);
                return false;
            }
        }
        orderRepository.save(order);
        productChangeList.forEach(product -> {
            Order2Product orderProduct = new Order2Product();
            orderProduct.setOrderId(order.getId());
            orderProduct.setProductId(product.getProduct().getId());
            orderProduct.setCount(product.getCount());
            productRepository.save(product.getProduct());
            order2ProductRepository.save(orderProduct);
        });
        return true;
    }

    private void returnCounts(List<ProductChangeDto> productChangeList) {
        for (ProductChangeDto productChange: productChangeList) {
            Product product = productChange.getProduct();
            product.setCount(product.getCount() + productChange.getCount());
        }
    }

    private boolean checkProductCount(Optional<Product> optProduct,
                                      ProductDto productDto,
                                      List<ProductChangeDto> productChangeList) throws InterruptedException {
        long latency = 20000 + new Random().nextLong(20000);
        logger.info("задержка поиска товара {} составила {} сек.", productDto.getProductId(), latency / 1000);
        Thread.sleep(latency);
        if (optProduct.isEmpty()) {
            return false;
        }
        Product product = optProduct.get();
        double count = product.getCount() - productDto.getCount();
        if (count < 0) {
            return false;
        }
        ProductChangeDto productChangeDto = new ProductChangeDto(product, productDto.getCount());
        product.setCount(count);
        productChangeList.add(productChangeDto);
        return true;
    }

}

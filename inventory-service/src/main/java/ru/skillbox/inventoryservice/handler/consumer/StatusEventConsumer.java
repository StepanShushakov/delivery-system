package ru.skillbox.inventoryservice.handler.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.skillbox.inventoryservice.domain.Product;
import ru.skillbox.inventoryservice.domain.dto.StatusDto;
import ru.skillbox.inventoryservice.domain.events.StatusEvent;
import ru.skillbox.inventoryservice.domain.links.Order2Product;
import ru.skillbox.inventoryservice.repository.Order2ProductRepository;
import ru.skillbox.inventoryservice.repository.ProductRepository;

import javax.transaction.Transactional;

import java.util.List;

import static ru.skillbox.inventoryservice.domain.enums.OrderStatus.DELIVERY_FAILED;
import static ru.skillbox.inventoryservice.domain.enums.ServiceName.DELIVERY_SERVICE;

@Component
@Slf4j
public class StatusEventConsumer implements EventConsumer<StatusEvent> {

    private final Order2ProductRepository order2ProductRepository;
    private final ProductRepository productRepository;

    @Autowired
    public StatusEventConsumer(Order2ProductRepository order2ProductRepository, ProductRepository productRepository) {
        this.order2ProductRepository = order2ProductRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public void consumeEvent(StatusEvent event) {
        StatusDto statusDto = event.getStatusDto();
        if ((statusDto.getServiceName() == DELIVERY_SERVICE
                && statusDto.getStatus() == DELIVERY_FAILED)) {
            rollbackInventory(event.getOrderId());
        }
    }

    private void rollbackInventory(Long orderId) {
        List<Order2Product> orderProducts = order2ProductRepository.findOrder2ProductByOrderId(orderId);
        List<Product> products = productRepository.findProductsByOrderId(orderId);
        for (Order2Product orderProduct: orderProducts) {
            Product product = findProduct(products, orderProduct.getProductId());
            assert product != null;
            product.setCount(product.getCount() + orderProduct.getCount());
        }
        productRepository.saveAll(products);
    }

    private Product findProduct(List<Product> products, int productId) {
        for (Product product: products) {
            if (product.getId() == productId) {
                return product;
            }
        }
        return null;
    }
}

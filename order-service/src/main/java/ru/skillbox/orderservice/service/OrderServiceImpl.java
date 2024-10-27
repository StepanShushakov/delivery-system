package ru.skillbox.orderservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.orderservice.domain.*;
import ru.skillbox.orderservice.domain.dto.OrderDto;
import ru.skillbox.orderservice.domain.dto.ProductDto;
import ru.skillbox.orderservice.domain.dto.ProductPrice;
import ru.skillbox.orderservice.domain.dto.ProductsPriceDto;
import ru.skillbox.orderservice.domain.enums.OrderStatus;
import ru.skillbox.orderservice.domain.enums.ServiceName;
import ru.skillbox.orderservice.domain.links.Order2Product;
import ru.skillbox.orderservice.processor.OrderProcessor;
import ru.skillbox.orderservice.repository.Order2ProductRepository;
import ru.skillbox.orderservice.repository.OrderRepository;
import ru.skillbox.orderservice.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderProcessor orderProcessor;
    private final ProductRepository productRepository;
    private final Order2ProductRepository order2ProductRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, OrderProcessor orderProcessor, ProductRepository productRepository, Order2ProductRepository order2ProductRepository) {
        this.orderRepository = orderRepository;
        this.orderProcessor = orderProcessor;
        this.productRepository = productRepository;
        this.order2ProductRepository = order2ProductRepository;
    }

    @Transactional
    @Override
    public Optional<Order> addOrder(OrderDto orderDto) {
        Order newOrder = new Order(
                orderDto.getDepartureAddress(),
                orderDto.getDestinationAddress(),
                orderDto.getDescription(),
                orderDto.getUserId(),
                OrderStatus.REGISTERED
        );

        newOrder.addStatusHistory(newOrder.getStatus(), ServiceName.ORDER_SERVICE, "Order created");
        Order order = orderRepository.save(newOrder);
        orderProcessor.process(order, setOrderDtoProductsOrderCost(order, orderDto));
        return Optional.of(order);
    }

    @Override
    public void setProductsPrice(ProductsPriceDto productsPriceDto) {
        List<Product> productList = new ArrayList<>();
        for (ProductPrice productPrice: productsPriceDto.getProductPriceList()) {
            Product product = getProduct(productPrice.getId());
            product.setPrice(productPrice.getPrice());
            productList.add(product);
        }
        productRepository.saveAll(productList);
    }

    private Product getProduct(Integer id) {
        return productRepository.findById(id).orElseGet(() -> new Product(id));
    }

    private List<ProductDto> setOrderDtoProductsOrderCost(Order order, OrderDto orderDto) {
        AtomicReference<Double> cost = new AtomicReference<>((double) 0);
        List<ProductDto> realProductsDtoList = new ArrayList<>();
        orderDto.getProducts().forEach(productDto -> {
            Optional<Product> optProduct = productRepository.findById(productDto.getProductId());
            if (optProduct.isPresent()) {
                realProductsDtoList.add(productDto);
                Order2Product orderProduct = new Order2Product();
                orderProduct.setOrderId(order.getId());
                orderProduct.setProductId(productDto.getProductId());
                orderProduct.setCount(productDto.getCount());
                order2ProductRepository.save(orderProduct);
                cost.set(cost.get() + productDto.getCount() * optProduct.get().getPrice());
            }
        });
        order.setCost(cost.get());
        orderRepository.save(order);
        return realProductsDtoList;
    }
}

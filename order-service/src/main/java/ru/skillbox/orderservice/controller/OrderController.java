package ru.skillbox.orderservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.orderservice.domain.Order;
import ru.skillbox.orderservice.domain.dto.OrderDto;
import ru.skillbox.orderservice.domain.dto.ProductsPriceDto;
import ru.skillbox.orderservice.repository.OrderRepository;
import ru.skillbox.orderservice.service.OrderService;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
public class OrderController {

    private final OrderRepository orderRepository;

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderRepository orderRepository, OrderService orderService) {
        this.orderRepository = orderRepository;
        this.orderService = orderService;
    }

    @Operation(summary = "List all orders in delivery system", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/order")
    public List<Order> listOrders() {
        return orderRepository.findAllOrders();
    }

    @Operation(summary = "Get an order in system by id", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/order/{orderId}")
    public Order listOrder(@PathVariable @Parameter(description = "Id of order") Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    @Operation(summary = "Add order and start delivery process for it", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/order")
    public ResponseEntity<?> addOrder(@RequestBody OrderDto input) {
        Optional<Order> result = orderService.addOrder(input);
        return result.isPresent() ? new ResponseEntity<>(HttpStatus.CREATED) : new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
    }

    @Operation(summary = "setting product price")
    @PostMapping("/price")
    public String setProductsPrice(@RequestBody ProductsPriceDto productsPriceDto) {
        orderService.setProductsPrice(productsPriceDto);
        return "цены установлены";
    }
}

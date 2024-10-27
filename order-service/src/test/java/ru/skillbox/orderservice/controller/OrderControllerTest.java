package ru.skillbox.orderservice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.skillbox.orderservice.domain.Order;
import ru.skillbox.orderservice.domain.dto.OrderDto;
import ru.skillbox.orderservice.domain.dto.ProductDto;
import ru.skillbox.orderservice.domain.enums.OrderStatus;
import ru.skillbox.orderservice.repository.OrderRepository;
import ru.skillbox.orderservice.service.OrderService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private OrderService orderService;

    @Configuration
    @ComponentScan(basePackageClasses = {OrderController.class})
    public static class TestConf {
    }

    private Order order;

    private Order newOrder;

    private List<Order> orders;

    @BeforeEach
    public void setUp() {
        order = new Order(
                "Moscow, st.Taganskaya 150",
                "Moscow, st.Tulskaya 24",
                "Order #112",
                1L,
                OrderStatus.REGISTERED
        );
        newOrder = new Order(
                "Moscow, st.Taganskaya 150",
                "Moscow, st.Dubininskaya 39",
                "Order #342",
                1L,
                OrderStatus.REGISTERED
        );
        orders = Collections.singletonList(order);

    }

    @Test
    public void listOrders() throws Exception {
        Mockito.when(orderRepository.findAllOrders()).thenReturn(orders);
        mvc.perform(get("/order"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(order.getDescription())));
    }

    @Test
    public void listOrder() throws Exception {
        Mockito.when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        mvc.perform(get("/order/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(order.getDescription())));
    }

    @Test
    public void addOrder() throws Exception {
        List<ProductDto> productDtoList = new ArrayList<>();
        productDtoList.add(new ProductDto(1, 2));
        OrderDto orderDto = new OrderDto(
                "Order #342",
                "Moscow, st.Taganskaya 150",
                "Moscow, st.Dubininskaya 39",
                1L,
                productDtoList
        );
        Mockito.when(orderService.addOrder(orderDto)).thenReturn(Optional.of(newOrder));
        mvc.perform(
                        post("/order")
                                .accept(MediaType.APPLICATION_JSON)
                                .content(
                                        "{\"description\":\"Order #342\",\"departureAddress\":\"Moscow, st.Taganskaya 150\"," +
                                                "\"destinationAddress\":\"Moscow, st.Dubininskaya 39\",\"cost\":2450,\"userId\":1," +
                                                "\"products\": [{\"productId\": 1 ,\"count\": 2}]}"
                                )
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated());
    }

}

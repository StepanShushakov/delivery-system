package ru.skillbox.paymentservice.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "orders")
public class Order {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "cost")
    private Double cost;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Order() {

    }

    public Order(Long id, Double cost, User user) {
        this.id = id;
        this.cost = cost;
        this.user = user;
    }
}

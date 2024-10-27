package ru.skillbox.orderservice.domain.links;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@ToString
@Entity
@Table(name = "order2product")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Order2Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "order_id", columnDefinition = "BIGINT NOT NULL")
    private long orderId;

    @Column(name = "product_id", columnDefinition = "INT NOT NULL")
    private int productId;

    @Column(name = "count", columnDefinition = "DOUBLE PRECISION")
    private double count;
}

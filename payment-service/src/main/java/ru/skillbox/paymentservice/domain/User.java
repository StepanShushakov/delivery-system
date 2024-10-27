
package ru.skillbox.paymentservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@ToString
@AllArgsConstructor
@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    private Long id;
    private double balance;
    @OneToMany(mappedBy = "user")
    private List<Order> orders;

    public User() {
    }
}

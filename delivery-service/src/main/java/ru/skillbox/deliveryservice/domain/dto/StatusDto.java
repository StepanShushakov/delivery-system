package ru.skillbox.deliveryservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skillbox.deliveryservice.domain.enums.OrderStatus;
import ru.skillbox.deliveryservice.domain.enums.ServiceName;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusDto {

    private OrderStatus status;

    private ServiceName serviceName;

    private String comment;
}

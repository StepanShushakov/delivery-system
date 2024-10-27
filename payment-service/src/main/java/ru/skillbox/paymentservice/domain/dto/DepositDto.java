package ru.skillbox.paymentservice.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DepositDto {

    @Schema(description = "current user id", example = "1")
    private Long userId;
    @Schema(description = "the amount to be credited to the balance", example = "100.51")
    private double sum;
}

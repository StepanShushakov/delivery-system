package ru.skillbox.paymentservice.service;

import ru.skillbox.paymentservice.domain.dto.DepositDto;

public interface UserService {

    double depositBalance(DepositDto depositDto);

    void addUser(Long userId);
}

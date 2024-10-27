package ru.skillbox.paymentservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.paymentservice.domain.dto.DepositDto;
import ru.skillbox.paymentservice.service.UserService;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "adding user")
    @PostMapping("/add/{userId}")
    public String add(@PathVariable Long userId) {
        userService.addUser(userId);
        return "пользователь добавлен";
    }

    @Operation(summary = "replenishment of the user's account balance")
    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestBody DepositDto depositDto) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.depositBalance(depositDto));

    }

}

package ru.skillbox.paymentservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.skillbox.paymentservice.domain.User;
import ru.skillbox.paymentservice.domain.dto.DepositDto;
import ru.skillbox.paymentservice.exception.UserFoundException;
import ru.skillbox.paymentservice.repository.UserRepository;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public double depositBalance(DepositDto depositDto) {
        Long userId = depositDto.getUserId();
        Optional<User> optUser = userRepository.findById(userId);
        User user;
        if (optUser.isEmpty()) {
            user = new User();
            user.setId(userId);
        }
        else {
            user = optUser.get();
        }
        user.setBalance(user.getBalance() + depositDto.getSum());
        userRepository.save(user);
        return user.getBalance();
    }

    @Override
    public void addUser(Long userId) {
        Optional<User> optUser = userRepository.findById(userId);
        if (optUser.isPresent()) {
            throw new UserFoundException(userId);
        }
        User user = new User();
        user.setId(userId);
        userRepository.save(user);
    }
}

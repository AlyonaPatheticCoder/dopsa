package com.laba.security;

import com.laba.dto.UserDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserClient {

    private final RabbitTemplate rabbitTemplate;

    public UserClient(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public Optional<UserDto> getUserByUsername(String username) {
        try {
            UserDto user = (UserDto) rabbitTemplate.convertSendAndReceive(
                    "user.getByUsername", username
            );
            return Optional.ofNullable(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public UserDto saveUser(UserDto userDto) {
        return (UserDto) rabbitTemplate.convertSendAndReceive(
                "user.save", userDto
        );
    }

    @SuppressWarnings("unchecked")
    public List<UserDto> getAllUsers() {
        Object response = rabbitTemplate.convertSendAndReceive("user.getAll", Optional.ofNullable(null));
        return response != null ? (List<UserDto>) response : Collections.emptyList();
    }
}
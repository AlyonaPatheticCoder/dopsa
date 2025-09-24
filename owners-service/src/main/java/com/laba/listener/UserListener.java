package com.laba.listener;

import com.laba.dto.UserDto;
import com.laba.dto.UserRequest;
import com.laba.dto.UserResponse;
import com.laba.impl.UserServiceImpl;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class UserListener {

    private final UserServiceImpl userService;
    public UserListener(UserServiceImpl userService) {
        this.userService = userService;
    }

    @RabbitListener(queues = "users-queue")
    public UserResponse handleUserRequest(UserRequest request) {
        try {
            return switch (request.getAction()) {
                case "GET_BY_ID" -> success(userService.getById(request.getId()));
                case "GET_ALL" -> success(userService.getAll());
                case "GET_BY_USERNAME" -> success(userService.findByUserName(request.getUsername()).orElse(null));
                case "SAVE" -> {
                    UserDto saved = userService.save(request.getUserDto());
                    yield success(saved);
                }
                case "UPDATE" -> {
                    UserDto updated = userService.update(request.getUserDto());
                    yield success(updated);
                }
                case "DELETE" -> {
                    userService.delete(request.getId());
                    yield success("User deleted");
                }
                default -> failure("Unknown action: " + request.getAction());
            };
        } catch (Exception e) {
            return failure(e.getMessage());
        }
    }

    private UserResponse success(Object data) {
        UserResponse response = new UserResponse();
        response.setSuccess(true);
        response.setData(data);
        return response;
    }

    private UserResponse failure(String message) {
        UserResponse response = new UserResponse();
        response.setSuccess(false);
        response.setMessage(message);
        return response;
    }
}
package com.laba.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.laba.dto.UserDto;
import com.laba.dto.UserRequest;
import com.laba.dto.UserResponse;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserClient {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    private static final String QUEUE = "users-queue";

    public UserClient(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    private UserResponse sendRequest(UserRequest request) {
        return (UserResponse) rabbitTemplate.convertSendAndReceive(QUEUE, request);
    }

    public UserDto getUserById(Long id) {
        UserRequest request = new UserRequest();
        request.setAction("GET_BY_ID");
        request.setId(id);
        Object data = sendRequest(request).getData();
        return objectMapper.convertValue(data, UserDto.class);
    }

    public List<UserDto> getAllUsers() {
        UserRequest request = new UserRequest();
        request.setAction("GET_ALL");
        Object data = sendRequest(request).getData();
        return objectMapper.convertValue(data, new TypeReference<List<UserDto>>() {});
    }

    public UserDto saveUser(UserDto userDto) {
        UserRequest request = new UserRequest();
        request.setAction("SAVE");
        request.setUserDto(userDto);
        Object data = sendRequest(request).getData();
        return objectMapper.convertValue(data, UserDto.class);
    }

    public UserDto updateUser(UserDto userDto) {
        UserRequest request = new UserRequest();
        request.setAction("UPDATE");
        request.setUserDto(userDto);
        Object data = sendRequest(request).getData();
        return objectMapper.convertValue(data, UserDto.class);
    }

    public void deleteUser(Long id) {
        UserRequest request = new UserRequest();
        request.setAction("DELETE");
        request.setId(id);
        sendRequest(request);
    }

    public Optional<UserDto> findByUserName(String username) {
        UserRequest request = new UserRequest();
        request.setAction("GET_BY_USERNAME");
        request.setUsername(username);
        Object data = sendRequest(request).getData();
        return data != null ? Optional.of(objectMapper.convertValue(data, UserDto.class)) : Optional.empty();
    }
}
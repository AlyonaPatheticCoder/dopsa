package com.laba.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.laba.dto.CatDto;
import com.laba.dto.CatRequest;
import com.laba.dto.CatResponse;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatClient {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    private static final String QUEUE = "cats-queue";

    public CatClient(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    private CatResponse sendRequest(CatRequest request) {
        return (CatResponse) rabbitTemplate.convertSendAndReceive(QUEUE, request);
    }

    public CatDto getCatById(Long id) {
        CatRequest request = new CatRequest();
        request.setAction("GET_BY_ID");
        request.setId(id);
        Object data = sendRequest(request).getData();
        return objectMapper.convertValue(data, CatDto.class);
    }

    public List<CatDto> getAllCats() {
        CatRequest request = new CatRequest();
        request.setAction("GET_ALL");
        Object data = sendRequest(request).getData();
        return objectMapper.convertValue(data, new TypeReference<List<CatDto>>() {});
    }

    public CatDto saveCat(CatDto catDto) {
        CatRequest request = new CatRequest();
        request.setAction("SAVE");
        request.setCatDto(catDto);
        Object data = sendRequest(request).getData();
        return objectMapper.convertValue(data, CatDto.class);
    }

    public CatDto updateCat(CatDto catDto) {
        CatRequest request = new CatRequest();
        request.setAction("UPDATE");
        request.setCatDto(catDto);
        Object data = sendRequest(request).getData();
        return objectMapper.convertValue(data, CatDto.class);
    }

    public void deleteCat(Long id) {
        CatRequest request = new CatRequest();
        request.setAction("DELETE");
        request.setId(id);
        sendRequest(request);
    }

    public List<CatDto> findCatsByName(String name) {
        CatRequest request = new CatRequest();
        request.setAction("FIND_BY_NAME");
        CatDto catDto = new CatDto();
        catDto.setName(name);
        request.setCatDto(catDto);
        Object data = sendRequest(request).getData();
        return objectMapper.convertValue(data, new TypeReference<List<CatDto>>() {});
    }

    public List<CatDto> findCatsByOwnerId(Long ownerId) {
        CatRequest request = new CatRequest();
        request.setAction("FIND_BY_OWNER_ID");
        request.setId(ownerId);
        Object data = sendRequest(request).getData();
        return objectMapper.convertValue(data, new TypeReference<List<CatDto>>() {});
    }

    public void addFriend(Long catId, Long friendId) {
        CatRequest request = new CatRequest();
        request.setAction("ADD_FRIEND");
        request.setId(catId);
        request.setFriendId(friendId);
        sendRequest(request);
    }

    public void removeFriend(Long catId, Long friendId) {
        CatRequest request = new CatRequest();
        request.setAction("REMOVE_FRIEND");
        request.setId(catId);
        request.setFriendId(friendId);
        sendRequest(request);
    }

    public void setOwner(Long catId, Long ownerId) {
        CatRequest request = new CatRequest();
        request.setAction("SET_OWNER");
        request.setId(catId);
        request.setOwnerId(ownerId);
        sendRequest(request);
    }

    public void removeOwner(Long catId) {
        CatRequest request = new CatRequest();
        request.setAction("REMOVE_OWNER");
        request.setId(catId);
        sendRequest(request);
    }
}
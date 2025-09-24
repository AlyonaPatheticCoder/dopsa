package com.laba.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.laba.dto.OwnerDto;
import com.laba.dto.OwnerRequest;
import com.laba.dto.OwnerResponse;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OwnerClient {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    private static final String QUEUE = "owners-queue";

    public OwnerClient(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    private OwnerResponse sendRequest(OwnerRequest request) {
        return (OwnerResponse) rabbitTemplate.convertSendAndReceive(QUEUE, request);
    }

    public OwnerDto getOwnerById(Long id) {
        OwnerRequest request = new OwnerRequest();
        request.setAction("GET_BY_ID");
        request.setId(id);
        Object data = sendRequest(request).getData();
        return objectMapper.convertValue(data, OwnerDto.class);
    }

    public List<OwnerDto> getAllOwners() {
        OwnerRequest request = new OwnerRequest();
        request.setAction("GET_ALL");
        Object data = sendRequest(request).getData();
        return objectMapper.convertValue(data, new TypeReference<List<OwnerDto>>() {});
    }

    public OwnerDto saveOwner(OwnerDto ownerDto) {
        OwnerRequest request = new OwnerRequest();
        request.setAction("SAVE");
        request.setOwnerDto(ownerDto);
        Object data = sendRequest(request).getData();
        return objectMapper.convertValue(data, OwnerDto.class);
    }

    public OwnerDto updateOwner(OwnerDto ownerDto) {
        OwnerRequest request = new OwnerRequest();
        request.setAction("UPDATE");
        request.setOwnerDto(ownerDto);
        Object data = sendRequest(request).getData();
        return objectMapper.convertValue(data, OwnerDto.class);
    }

    public void deleteOwner(Long id) {
        OwnerRequest request = new OwnerRequest();
        request.setAction("DELETE");
        request.setId(id);
        sendRequest(request);
    }

    public List<OwnerDto> findOwnersByName(String name) {
        OwnerRequest request = new OwnerRequest();
        request.setAction("FIND_BY_NAME");
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setName(name);
        request.setOwnerDto(ownerDto);
        Object data = sendRequest(request).getData();
        return objectMapper.convertValue(data, new TypeReference<List<OwnerDto>>() {});
    }

    public OwnerDto findOwnerByCatId(Long catId) {
        OwnerRequest request = new OwnerRequest();
        request.setAction("FIND_BY_CAT_ID");
        request.setCatId(catId);
        Object data = sendRequest(request).getData();
        return objectMapper.convertValue(data, OwnerDto.class);
    }

    public List<OwnerDto> findOwnersByFilter(OwnerDto filter) {
        OwnerRequest request = new OwnerRequest();
        request.setAction("FIND_BY_FILTER");
        request.setOwnerDto(filter);
        Object data = sendRequest(request).getData();
        return objectMapper.convertValue(data, new TypeReference<List<OwnerDto>>() {});
    }

    public void addCatToOwner(Long ownerId, Long catId) {
        OwnerRequest request = new OwnerRequest();
        request.setAction("ADD_CAT");
        request.setOwnerId(ownerId);
        request.setCatId(catId);
        sendRequest(request);
    }

    public void removeCatFromOwner(Long ownerId, Long catId) {
        OwnerRequest request = new OwnerRequest();
        request.setAction("REMOVE_CAT");
        request.setOwnerId(ownerId);
        request.setCatId(catId);
        sendRequest(request);
    }
}
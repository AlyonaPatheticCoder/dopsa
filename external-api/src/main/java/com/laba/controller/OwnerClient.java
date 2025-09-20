package com.laba.controller;

import com.laba.dto.OwnerDto;
import com.laba.dto.OwnerRequest;
import com.laba.dto.OwnerResponse;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OwnerClient {

    private final RabbitTemplate rabbitTemplate;

    private static final String EXCHANGE = "";
    private static final String QUEUE = "owners-queue";

    public OwnerClient(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    private OwnerResponse sendRequest(OwnerRequest request) {
        return (OwnerResponse) rabbitTemplate.convertSendAndReceive(QUEUE, request);
    }

    public OwnerDto getOwnerById(Long id) {
        OwnerRequest request = new OwnerRequest();
        request.setAction("GET_BY_ID");
        request.setId(id);
        return (OwnerDto) sendRequest(request).getData();
    }

    public List<OwnerDto> getAllOwners() {
        OwnerRequest request = new OwnerRequest();
        request.setAction("GET_ALL");
        return (List<OwnerDto>) sendRequest(request).getData();
    }

    public OwnerDto saveOwner(OwnerDto ownerDto) {
        OwnerRequest request = new OwnerRequest();
        request.setAction("SAVE");
        request.setOwnerDto(ownerDto);
        return (OwnerDto) sendRequest(request).getData();
    }

    public void updateOwner(OwnerDto ownerDto) {
        OwnerRequest request = new OwnerRequest();
        request.setAction("UPDATE");
        request.setOwnerDto(ownerDto);
        sendRequest(request);
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
        return (List<OwnerDto>) sendRequest(request).getData();
    }

    public OwnerDto findOwnerByCatId(Long catId) {
        OwnerRequest request = new OwnerRequest();
        request.setAction("FIND_BY_CAT_ID");
        request.setCatId(catId);
        return (OwnerDto) sendRequest(request).getData();
    }

    public List<OwnerDto> findOwnersByFilter(OwnerDto filter) {
        OwnerRequest request = new OwnerRequest();
        request.setAction("FIND_BY_FILTER");
        request.setOwnerDto(filter);
        return (List<OwnerDto>) sendRequest(request).getData();
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
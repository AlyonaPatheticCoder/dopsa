package com.laba.listener;

import com.laba.dto.OwnerDto;
import com.laba.dto.OwnerRequest;
import com.laba.dto.OwnerResponse;
import com.laba.service.OwnerService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OwnerListener {

    private final OwnerService ownerService;

    @Autowired
    public OwnerListener(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @RabbitListener(queues = "owners-queue")
    public OwnerResponse handleOwnerRequest(OwnerRequest request) {
        try {
            return switch (request.getAction()) {
                case "GET_BY_ID" -> success(ownerService.getOwnerById(request.getId()));
                case "GET_ALL" -> success(ownerService.getAllOwners());
                case "FIND_BY_FILTER" -> success(ownerService.findOwnersByFilter(request.getOwnerDto()));
                case "FIND_BY_NAME" -> success(ownerService.findOwnersByName(request.getOwnerDto().getName()));
                case "FIND_BY_CAT_ID" -> success(ownerService.findOwnerByCatId(request.getCatId()));
                case "SAVE" -> {
                    ownerService.saveOwner(request.getOwnerDto());
                    yield success("Owner saved");
                }
                case "UPDATE" -> {
                    ownerService.updateOwner(request.getOwnerDto());
                    yield success("Owner updated");
                }
                case "DELETE" -> {
                    ownerService.deleteOwner(request.getId());
                    yield success("Owner deleted");
                }
                case "ADD_CAT" -> {
                    ownerService.addCatToOwner(request.getId(), request.getCatId());
                    yield success("Cat added to owner successfully");
                }
                case "REMOVE_CAT" -> {
                    ownerService.removeCatFromOwner(request.getId(), request.getCatId());
                    yield success("Cat removed from owner successfully");
                }
                default -> failure("Unknown action: " + request.getAction());
            };
        } catch (Exception e) {
            return failure(e.getMessage());
        }
    }

    private OwnerResponse success(Object data) {
        OwnerResponse response = new OwnerResponse();
        response.setSuccess(true);
        response.setData(data);
        return response;
    }

    private OwnerResponse failure(String message) {
        OwnerResponse response = new OwnerResponse();
        response.setSuccess(false);
        response.setMessage(message);
        return response;
    }
}
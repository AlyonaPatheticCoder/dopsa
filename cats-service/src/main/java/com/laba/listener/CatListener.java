package com.laba.listener;
import com.laba.dto.CatDto;
import com.laba.dto.CatRequest;
import com.laba.dto.CatResponse;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.laba.service.CatService;

import java.util.List;


@Service
public class CatListener {

    private final CatService catService;

    @Autowired
    public CatListener(CatService catService) {
        this.catService = catService;
    }

    @RabbitListener(queues = "cats-queue")
    public CatResponse handleCatRequest(CatRequest request) {
        try {
            switch (request.getAction()) {
                case "GET_BY_ID" -> {
                    CatDto cat = catService.getCatById(request.getId());
                    return success(cat);
                }
                case "GET_ALL" -> {
                    List<CatDto> cats = catService.getAllCats();
                    return success(cats);
                }
                case "FIND_BY_NAME" -> {
                    List<CatDto> cats = catService.findCatsByName(request.getCatDto().getName());
                    return success(cats);
                }
                case "FIND_BY_OWNER_NAME" -> {
                    List<CatDto> cats = catService.findCatsByOwnerName(request.getCatDto().getOwner().getName());
                    return success(cats);
                }
                case "FIND_BY_OWNER_ID" -> {
                    List<CatDto> cats = catService.findCatsByOwnerId(request.getId());
                    return success(cats);
                }
                case "SAVE" -> {
                    CatDto saved = catService.saveCat(request.getCatDto());
                    return success(saved);
                }
                case "UPDATE" -> {
                    CatDto updated = catService.updateCat(request.getCatDto());
                    return success(updated);
                }
                case "DELETE" -> {
                    catService.deleteCat(request.getId());
                    return success("Cat deleted");
                }
                case "ADD_FRIEND" -> {
                    catService.addFriend(request.getId(), request.getFriendId());
                    return success("Friend added");
                }
                case "REMOVE_FRIEND" -> {
                    catService.removeFriend(request.getId(), request.getFriendId());
                    return success("Friend removed");
                }
                default -> {
                    return failure("Unknown action: " + request.getAction());
                }
            }
        } catch (Exception e) {
            return failure(e.getMessage());
        }
    }

    private CatResponse success(Object data) {
        CatResponse response = new CatResponse();
        response.setSuccess(true);
        response.setData(data);
        return response;
    }

    private CatResponse failure(String message) {
        CatResponse response = new CatResponse();
        response.setSuccess(false);
        response.setMessage(message);
        return response;
    }
}
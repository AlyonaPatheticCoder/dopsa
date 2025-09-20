package com.laba.dto;

import java.util.ArrayList;
import java.util.List;

public class OwnerRequest {
    private String action;
    private OwnerDto ownerDto;
    private Long id;
    private Long ownerId;
    private Long catId;
    private List<Long> catIds = new ArrayList<>();

    public Long getOwnerId() { return ownerId; }
    public void setOwnerId(Long ownerId) { this.ownerId = ownerId; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public OwnerDto getOwnerDto() { return ownerDto; }
    public void setOwnerDto(OwnerDto ownerDto) { this.ownerDto = ownerDto; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public List<Long> getCatIds() { return catIds; }
    public void setCatIds(List<Long> catIds) { this.catIds = catIds; }

    public Long getCatId() {return catId;}

    public void setCatId(Long catId) { this.catId = catId;
    }
}

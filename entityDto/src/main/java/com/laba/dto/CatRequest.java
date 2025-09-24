package com.laba.dto;

public class CatRequest {
    private String action;
    private CatDto catDto;
    private Long id;
    private Long ownerId;
    private Long friendId;

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public CatDto getCatDto() { return catDto; }
    public void setCatDto(CatDto catDto) { this.catDto = catDto; }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getOwnerId() { return ownerId; }
    public void setOwnerId(Long ownerId) { this.ownerId = ownerId; }
    public Long getFriendId() { return friendId; }
    public void setFriendId(Long friendId) { this.friendId = friendId; }
}

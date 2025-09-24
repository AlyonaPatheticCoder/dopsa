package com.laba.dto;


public class UserRequest {
    private String action;
    private UserDto userDto;
    private Long id;
    private String username;

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public UserDto getUserDto() { return userDto; }
    public void setUserDto(UserDto userDto) { this.userDto = userDto; }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}
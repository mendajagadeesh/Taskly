package com.jagdev.Task_project.service;
import com.jagdev.Task_project.payload.UserDto;
import org.springframework.web.bind.annotation.RequestBody;

public interface UserService {
    public UserDto createUser(@RequestBody UserDto userdto);
}

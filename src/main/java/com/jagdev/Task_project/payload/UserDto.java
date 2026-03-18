package com.jagdev.Task_project.payload;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserDto {
    private int id;
    private String name;
    private String email;
    private String password;
    private Set<String> roles;
}

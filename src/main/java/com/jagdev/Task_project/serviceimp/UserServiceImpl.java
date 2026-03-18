package com.jagdev.Task_project.serviceimp;

import com.jagdev.Task_project.entity.Users;
import com.jagdev.Task_project.payload.UserDto;
import com.jagdev.Task_project.repository.UsersRepository;
import com.jagdev.Task_project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDto createUser(UserDto userdto) {
        userdto.setPassword(passwordEncoder.encode(userdto.getPassword()));
        Users users=userDtoToEntity(userdto);

        if(userdto.getRoles()!=null && !userdto.getRoles().isEmpty()){
            users.setRoles(userdto.getRoles());
        }
        else{
            users.setRoles(Set.of("ROLE_USER"));
        }
        Users savedUser=usersRepository.save(users);
        return usersEntityToDto(savedUser);
    }

    private Users userDtoToEntity(UserDto userdto) {
        Users users = new Users();
        users.setName(userdto.getName());
        users.setEmail(userdto.getEmail());
        users.setPassword(userdto.getPassword());
        return users;
    }

    private UserDto usersEntityToDto(Users users) {
        UserDto userdto = new UserDto();
        userdto.setId(users.getId());
        userdto.setName(users.getName());
        userdto.setEmail(users.getEmail());
        userdto.setRoles(users.getRoles());
        return userdto;
    }
}

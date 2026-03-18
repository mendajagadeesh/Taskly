package com.jagdev.Task_project.serviceimp;

import com.jagdev.Task_project.entity.Task;
import com.jagdev.Task_project.entity.Users;
import com.jagdev.Task_project.exception.ApiException;
import com.jagdev.Task_project.exception.TaskNotFound;
import com.jagdev.Task_project.exception.UserNotFound;
import com.jagdev.Task_project.payload.TaskDto;
import com.jagdev.Task_project.repository.TaskRepository;
import com.jagdev.Task_project.repository.UsersRepository;
import com.jagdev.Task_project.service.TaskService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private TaskRepository  taskRepository;
    @Override
    public TaskDto saveTask(int userid, TaskDto taskDto) {
        Users user=usersRepository.findById(userid).orElseThrow(
                () ->new UserNotFound(String.format("User  id %d not found", userid))
        );
        Task task=modelMapper.map(taskDto, Task.class);
        task.setUsers(user);
       Task savedTask= taskRepository.save(task);
        return modelMapper.map(savedTask, TaskDto.class);
    }


    @Override
    public List<TaskDto> getAllTasks(int userid) {
        Users user = usersRepository.findById(userid).orElseThrow(
                () -> new UserNotFound(String.format("User id %d not found", userid))
        );

        List<Task> tasks = taskRepository.findByUsersId(userid);

        return tasks.stream()
                .map(task -> modelMapper.map(task, TaskDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public TaskDto getTask(int userid, int taskid) {
        Users user=usersRepository.findById(userid).orElseThrow(
                () -> new UserNotFound(String.format("User  id %d not found", userid))
        );
        Task task=taskRepository.findById(taskid).orElseThrow(
                () -> new TaskNotFound(String.format("Task  id %d not found", taskid))
        );

        if(user.getId()!=task.getUsers().getId()){
           throw new ApiException(String.format("Task id %d not belongs to user id %d", taskid, userid));
        }
        return modelMapper.map(task,TaskDto.class);
    }

    @Override
    public void deleteTask(int userid, int taskid) {
        Users user=usersRepository.findById(userid).orElseThrow(
                () -> new UserNotFound(String.format("User  id %d not found", userid))
        );
        Task task=taskRepository.findById(taskid).orElseThrow(
                () -> new TaskNotFound(String.format("Task  id %d not found", taskid))
        );

        if(user.getId()!=task.getUsers().getId()){
            throw new ApiException(String.format("Task id %d not belongs to user id %d", taskid, userid));
        }
        taskRepository.deleteById(taskid);

    }
}

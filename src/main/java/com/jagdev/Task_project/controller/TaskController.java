package com.jagdev.Task_project.controller;

import com.jagdev.Task_project.payload.TaskDto;
import com.jagdev.Task_project.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/{userid}/tasks")
    public ResponseEntity<TaskDto> saveTask(@PathVariable int userid, @RequestBody TaskDto taskDto) {
        return new ResponseEntity<>(taskService.saveTask(userid, taskDto), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('USER','ADMIN')")
    @GetMapping("/{userid}/tasks")
    public ResponseEntity<List<TaskDto>> getAllTasks(@PathVariable int userid) {
        return new ResponseEntity<>(taskService.getAllTasks(userid), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER','ADMIN')")
    @GetMapping("/{userid}/tasks/{taskid}")
    public ResponseEntity<TaskDto> getTask(@PathVariable int userid,
                                           @PathVariable int taskid) {
      return new ResponseEntity<>(taskService.getTask(userid, taskid), HttpStatus.OK);
    }


    @PreAuthorize("hasRole('USER','ADMIN')")
    @DeleteMapping("/{userid}/tasks/{taskid}")
    public ResponseEntity<String> deleteTask(@PathVariable int userid,
                                           @PathVariable int taskid) {
        taskService.deleteTask(userid, taskid);
        return new ResponseEntity<>("User's Task Deleted successfully", HttpStatus.OK);
    }

}

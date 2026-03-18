package com.jagdev.Task_project.service;

import com.jagdev.Task_project.payload.TaskDto;

import java.util.List;

public interface TaskService {
    public TaskDto saveTask(int userid, TaskDto taskDto);

    public List<TaskDto> getAllTasks(int userid);

    public TaskDto getTask(int userid,int taskid);

    public void deleteTask(int userid, int taskid);
}

package com.jagdev.Task_project.repository;

import com.jagdev.Task_project.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {


    List<Task> findByUsersId(int userid);
}

package com.testproject.testproject.repository;

import com.testproject.testproject.model.Task;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends CrudRepository<Task, Integer> {
    List<Task> findByIdWorker(Integer idWorker);
}

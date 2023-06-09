package com.testproject.testproject.repository;

import com.testproject.testproject.model.Worker;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkerRepository extends CrudRepository<Worker, Integer> {

}

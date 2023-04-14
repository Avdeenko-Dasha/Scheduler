package com.testproject.testproject.service;

import com.testproject.testproject.model.Worker;
import com.testproject.testproject.repository.WorkerRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkerServiceImpl {

    @Autowired
    private WorkerRepository workerRepository;

    public List<Worker> findAll() {
        return (List<Worker>) workerRepository.findAll();
    }

    public List<String> getDataForWorkerCom() {
        List<Worker> workers = findAll();
        List<String> workersToString = new ArrayList<>();

        for (Worker worker : workers) {
            workersToString.add(worker.getInitials() + " " + worker.getSurname());
        }

        return workersToString;
    }

    public Worker findByInitialsAndSurname(String initialsAndSurname) {
        List<Worker> workers = findAll();
        for (Worker worker : workers) {
            if (initialsAndSurname.equals(worker.getInitials() + " " + worker.getSurname())) {
                return worker;
            }
        }
        return null;
    }
}

package com.testproject.testproject.service;

import com.testproject.testproject.model.Task;
import com.testproject.testproject.model.Worker;
import com.testproject.testproject.repository.TaskRepository;
import jakarta.persistence.Query;
import jakarta.websocket.Session;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@Service
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskServiceImpl {

    @Autowired
    private TaskRepository taskRepository;

    public Object[][] getTasksForWorker(Worker worker) {
        List<Task> tasks = taskRepository.findByIdWorker(worker.getId());
        int numberOfTasks = tasks.size();
        Object[][] tasksInTable = new Object[numberOfTasks][5];

        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        for (int i = 0; i < numberOfTasks; i++) {
            tasksInTable[i][0] = tasks.get(i).getDone();
            tasksInTable[i][1] = tasks.get(i).getName();
            tasksInTable[i][2] = dateFormat.format(tasks.get(i).getStartDate());
            tasksInTable[i][3] = dateFormat.format(tasks.get(i).getFinishDate());
            tasksInTable[i][4] = dateFormat.format(tasks.get(i).getCompletionDate());
        }

        return tasksInTable;
    }

    public String[][] getTasksForPlane(Worker worker, Integer monthNumber) {
        List<Task> tasks = taskRepository.findByIdWorker(worker.getId());
        YearMonth yearMonth = YearMonth.now().withMonth(monthNumber);
        int numberOfTasks = tasks.size();
        int numberOfDays = yearMonth.lengthOfMonth();
        String[][] tableForPlane = new String[numberOfTasks][numberOfDays + 1];

        monthNumber = monthNumber - 1;

        for (int i = 0; i < numberOfTasks; i++) {
            tableForPlane[i][0] = tasks.get(i).getName();
            Date startDate = tasks.get(i).getStartDate();
            Date finishDate = tasks.get(i).getFinishDate();
            for (int j = 1; j < numberOfDays + 1; j++) {
                if (startDate.getMonth() == monthNumber) {
                    if (finishDate.getMonth() == monthNumber) {
                        if (j >= startDate.getDate() && j <= finishDate.getDate()) {
                            tableForPlane[i][j] = " ";
                        } else {
                            tableForPlane[i][j] = "";
                        }
                    } else {
                        if (j >= startDate.getDate()) {
                            tableForPlane[i][j] = " ";
                        } else {
                            tableForPlane[i][j] = "";
                        }
                    }
                } else {
                    if (finishDate.getMonth() == monthNumber) {
                        if (j <= finishDate.getDate()) {
                            tableForPlane[i][j] = " ";
                        } else {
                            tableForPlane[i][j] = "";
                        }
                    } else {
                        tableForPlane[i][j] = "";
                    }
                }
            }
        }

        return tableForPlane;
    }

    public void updateDataForTaskTable(Worker worker, Object newData, int row, int col) throws ParseException {
        List<Task> tasks = taskRepository.findByIdWorker(worker.getId());
        Task task = tasks.get(row);
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        switch (col) {
            case 0:
                task.setDone((Boolean) newData);
                break;
            case 1:
                task.setName(newData.toString());
                break;
            case 2:
                task.setStartDate(dateFormat.parse(newData.toString()));
                break;
            case 3:
                task.setFinishDate(dateFormat.parse(newData.toString()));
                break;
            case 4:
                task.setCompletionDate(dateFormat.parse(newData.toString()));
                break;
        }
        taskRepository.save(task);
    }
}

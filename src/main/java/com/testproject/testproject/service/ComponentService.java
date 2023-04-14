package com.testproject.testproject.service;

import com.testproject.testproject.ui.CellRenderer.TableCellRenderer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.YearMonth;

@Service
@Getter
@Setter
//@NoArgsConstructor
//@AllArgsConstructor
@RequiredArgsConstructor
public class ComponentService {

    private final TaskServiceImpl taskService;
    private final WorkerServiceImpl workerService;

    public JTable createTableForTasks() {
        String[] namesForTableTask = {"Выполнена", "Задача", "Дата начала",
            "Дата оканчания", "Дата выполнения"};

        JTable tableForTasks = new JTable();
        DefaultTableModel modelForTableTask = new DefaultTableModel() {
            public Class<?> getColumnClass(int column) {
                switch (column) {
                    case 0:
                        return Boolean.class;
                    case 1:
                        return String.class;
                    case 2:
                        return String.class;
                    case 3:
                        return String.class;
                    case 4:
                        return String.class;
                    default:
                        return String.class;
                }
            }
        };

        JCheckBox checkBoxForDone = new JCheckBox();
        tableForTasks.setModel(modelForTableTask);
        DefaultCellEditor editor = new DefaultCellEditor(checkBoxForDone);

        modelForTableTask.addColumn(editor);
        modelForTableTask.addColumn("Задача");
        modelForTableTask.addColumn("Дата начала");
        modelForTableTask.addColumn("Дата оканчания");
        modelForTableTask.addColumn("Дата выполнения");

        modelForTableTask.setColumnIdentifiers(namesForTableTask);

        Object[][] dataForTableForTasks = taskService.getTasksForWorker(workerService.findAll().get(0));

        for (int i = 0; i < dataForTableForTasks.length; i++) {
            modelForTableTask.addRow(new Object[0]);
            modelForTableTask.setValueAt(dataForTableForTasks[i][0], i, 0);
            modelForTableTask.setValueAt(dataForTableForTasks[i][1], i, 1);
            modelForTableTask.setValueAt(dataForTableForTasks[i][2], i, 2);
            modelForTableTask.setValueAt(dataForTableForTasks[i][3], i, 3);
            modelForTableTask.setValueAt(dataForTableForTasks[i][4], i, 4);
        }
        tableForTasks.setIntercellSpacing(new Dimension(1, 1));

        tableForTasks.getColumnModel().getColumn(0).setPreferredWidth(1);

        return tableForTasks;
    }

    public JTable createTableForPlan() {
        DefaultTableModel modelForTablePlane = new DefaultTableModel(taskService.getTasksForPlane(workerService.findAll().get(0), 1),
            getHeaderForPlaneTable(1));

        JTable tableForPlan = new JTable(modelForTablePlane);
        tableForPlan.getColumnModel().getColumn(0).setPreferredWidth(200);

        TableCellRenderer tableCellRenderer = new TableCellRenderer();
        for (int j = 1; j < tableForPlan.getColumnCount(); j++) {
            tableForPlan.getColumnModel().getColumn(j).setCellRenderer(tableCellRenderer);
        }

        tableForPlan.setCellSelectionEnabled(false);

        return tableForPlan;
    }

    public String[] getHeaderForPlaneTable(Integer monthNumber) {
        YearMonth yearMonth = YearMonth.now().withMonth(monthNumber);
        int numberOfDays = yearMonth.lengthOfMonth();
        String[] daysOfMonth = new String[numberOfDays + 1];
        daysOfMonth[0] = "Задача";
        for (int i = 1; i < numberOfDays + 1; i++) {
            daysOfMonth[i] = String.valueOf(i);
        }
        return daysOfMonth;
    }
}

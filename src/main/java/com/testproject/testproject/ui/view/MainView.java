package com.testproject.testproject.ui.view;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.testproject.testproject.model.Worker;
import com.testproject.testproject.service.ComponentService;
import com.testproject.testproject.service.MonthService;
import com.testproject.testproject.service.TaskServiceImpl;
import com.testproject.testproject.service.WorkerServiceImpl;
import com.testproject.testproject.ui.CellRenderer.TableCellRenderer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.text.ParseException;

@Component
@AllArgsConstructor
@Getter
@Setter
public class MainView extends JFrame {
    private JPanel panelWithAllInfo;
    private JComboBox workerComboBox;
    private JTabbedPane tabbedPanel;
    private JTable tableForTasks;
    private JComboBox monthComboBox;
    private JTable tableForPlan;

    private final TaskServiceImpl taskService;
    private final WorkerServiceImpl workerService;
    private final MonthService monthService;
    private final ComponentService componentService;

    @Autowired
    public MainView(TaskServiceImpl taskService, WorkerServiceImpl workerService,
                    MonthService monthService, ComponentService componentService) {
        this.taskService = taskService;
        this.workerService = workerService;
        this.monthService = monthService;
        this.componentService = componentService;

        final JPanel panelForTableTask = new JPanel();
        panelForTableTask.setLayout(new BorderLayout());

        tableForTasks = componentService.createTableForTasks();
        DefaultTableModel modelForTableTask = (DefaultTableModel) tableForTasks.getModel();

        modelForTableTask.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                int row = e.getFirstRow();
                int col = e.getColumn();
                TableModel model = (TableModel) e.getSource();
                Object newData = model.getValueAt(row, col);

                String selectedItem = (String) workerComboBox.getSelectedItem();
                Worker worker = MainView.this.workerService.findByInitialsAndSurname(selectedItem);

                try {
                    MainView.this.taskService.updateDataForTaskTable(worker, newData, row, col);
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
            }
        });

        panelForTableTask.add(new JScrollPane(tableForTasks), BorderLayout.CENTER);
        panelForTableTask.add(tableForTasks.getTableHeader(), BorderLayout.NORTH);

        tableForPlan = componentService.createTableForPlan();

        workerComboBox = new JComboBox(workerService.getDataForWorkerCom().toArray());
        workerComboBox.addActionListener(e -> {
            {
                String selectedItem = (String) workerComboBox.getSelectedItem();
                Worker worker = MainView.this.workerService.findByInitialsAndSurname(selectedItem);
                //DefaultTableModel modelForTableTask = (DefaultTableModel) MainView.this.tableForTasks.getModel();
                Object[][] newDataForTableTask = MainView.this.taskService.getTasksForWorker(worker);

                for (int i = 0; i < newDataForTableTask.length; i++) {
                    for (int j = 0; j < newDataForTableTask[i].length; j++) {
                        modelForTableTask.setValueAt(newDataForTableTask[i][j], i, j);
                    }
                }

                MainView.this.tableForTasks.getColumnModel().getColumn(0).setPreferredWidth(1);

                String selectedItemForMonth = (String) monthComboBox.getSelectedItem();
                Integer numberMonth = MainView.this.monthService.getNumberMonth(selectedItemForMonth);

                String[][] newDataForTablePlan = MainView.this.taskService.getTasksForPlane(worker, numberMonth);

                String[] headerForNewTablePlan = MainView.this.componentService.getHeaderForPlaneTable(numberMonth);

                DefaultTableModel modelForTablePlan = new DefaultTableModel(newDataForTablePlan, headerForNewTablePlan);

                MainView.this.tableForPlan.setModel(modelForTablePlan);

                TableCellRenderer tableCellRenderer1 = new TableCellRenderer();
                for (int j = 1; j < MainView.this.tableForPlan.getColumnCount(); j++) {
                    MainView.this.tableForPlan.getColumnModel().getColumn(j).setCellRenderer(tableCellRenderer1);
                }
                MainView.this.tableForPlan.getColumnModel().getColumn(0).setPreferredWidth(200);
            }
        });

        final JPanel panelForComboBox = new JPanel();
        panelForComboBox.setLayout(new GridLayoutManager(1, 3));

        JLabel textForWorker = new JLabel("Сотрудник: ");
        panelForComboBox.add(textForWorker, new GridConstraints(0, 0, 1, 1,
            GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
            GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK,
            null, null, null, 0, false));

        panelForComboBox.add(workerComboBox,
            new GridConstraints(0, 1, 1, 1,
                GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
                GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_GROW,
                null, null, null, 0, false));

        final Spacer spacerAfterWorkerComboBox = new Spacer();
        panelForComboBox.add(spacerAfterWorkerComboBox, new GridConstraints(0, 2, 1, 1,
            GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
            GridConstraints.SIZEPOLICY_WANT_GROW, 1,
            null, null, null, 0, false));

        panelWithAllInfo = new JPanel();
        panelWithAllInfo.setLayout(new GridLayoutManager(3, 1,
            new Insets(0, 0, 0, 0), -1, -1));


        panelWithAllInfo.add(panelForComboBox,
            new GridConstraints(0, 0, 1, 1,
                GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
                GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_GROW,
                null, null, null, 0, false));

        tabbedPanel = new JTabbedPane();
        panelWithAllInfo.add(tabbedPanel,
            new GridConstraints(1, 0, 1, 1,//2
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                null, new Dimension(200, 200), null, 0, false));


        final JPanel panelWithTabbedForTasks = new JPanel();

        panelWithTabbedForTasks.setLayout(
            new GridLayoutManager(1, 1,
                new Insets(0, 0, 0, 0),
                -1, -1));

        tabbedPanel.addTab("Задачи", panelWithTabbedForTasks);

        panelWithTabbedForTasks.add(panelForTableTask,
            new GridConstraints(0, 0, 1, 1,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW,
                null, new Dimension(150, 50),
                null, 0, false));

        final JPanel panelWithTabbedForPlane = new JPanel();
        panelWithTabbedForPlane.setLayout(
            new GridLayoutManager(2, 2,
                new Insets(0, 0, 0, 0),
                -1, -1));

        tabbedPanel.addTab("План", panelWithTabbedForPlane);

        JPanel panelForComboMonth = new JPanel();
        panelForComboMonth.setLayout(new GridLayoutManager(1, 3));

        JPanel panelForTableForPlan = new JPanel();
        panelForTableForPlan.setLayout(new BorderLayout());

        panelForTableForPlan.add(new JScrollPane(tableForPlan), BorderLayout.CENTER);
        panelForTableForPlan.add(tableForPlan.getTableHeader(), BorderLayout.NORTH);

        monthComboBox = new JComboBox(monthService.getNamesAllMonth());
        monthComboBox.addActionListener(e -> {
            String selectedItem = (String) monthComboBox.getSelectedItem();
            Integer numberMonth = MainView.this.monthService.getNumberMonth(selectedItem);

            String[][] newDataForTable = MainView.this.taskService.getTasksForPlane(
                MainView.this.workerService.findByInitialsAndSurname(
                    MainView.this.workerComboBox.getSelectedItem().toString()
                ), numberMonth);

            String[] headerForNewTable = MainView.this.componentService.getHeaderForPlaneTable(numberMonth);

            DefaultTableModel model = new DefaultTableModel(newDataForTable, headerForNewTable);

            tableForPlan.setModel(model);

            TableCellRenderer tableCellRenderer1 = new TableCellRenderer();
            for (int j = 1; j < tableForPlan.getColumnCount(); j++) {
                tableForPlan.getColumnModel().getColumn(j).setCellRenderer(tableCellRenderer1);
            }

            MainView.this.tableForPlan.getColumnModel().getColumn(0).setPreferredWidth(200);
            MainView.this.tableForPlan.setCellSelectionEnabled(false);
        });

        JLabel textForMonth = new JLabel("Месяц: ");
        panelForComboMonth.add(textForMonth, new GridConstraints(0, 0, 1, 1,
            GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
            GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK,
            null, null, null, 0, false));

        panelForComboMonth.add(monthComboBox, new GridConstraints(0, 1, 1, 1,
            GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
            GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED,
            null, null, null, 0, false));

        final Spacer spacerAfterMonthComboBox = new Spacer();
        panelForComboMonth.add(spacerAfterMonthComboBox, new GridConstraints(0, 2, 1, 1,
            GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
            GridConstraints.SIZEPOLICY_WANT_GROW, 1,
            null, null, null, 0, false));

        panelWithTabbedForPlane.add(panelForComboMonth,
            new GridConstraints(0, 0, 1, 1,
                GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
                GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED,
                null, null, null, 0, false));

        panelWithTabbedForPlane.add(panelForTableForPlan,
            new GridConstraints(1, 0, 1, 1,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW,
                null, new Dimension(150, 50), null, 0, false));

        tabbedPanel.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int tabIndex = tabbedPanel.getSelectedIndex();
                if (tabIndex == 1) {
                    String selectedItem = (String) monthComboBox.getSelectedItem();
                    Integer numberMonth = MainView.this.monthService.getNumberMonth(selectedItem);

                    String[][] newDataForTable = MainView.this.taskService.getTasksForPlane(
                        MainView.this.workerService.findByInitialsAndSurname(
                            MainView.this.workerComboBox.getSelectedItem().toString()
                        ), numberMonth);

                    String[] headerForNewTable = MainView.this.componentService.getHeaderForPlaneTable(numberMonth);

                    DefaultTableModel model = new DefaultTableModel(newDataForTable, headerForNewTable);

                    tableForPlan.setModel(model);

                    TableCellRenderer tableCellRenderer1 = new TableCellRenderer();
                    for (int j = 1; j < tableForPlan.getColumnCount(); j++) {
                        tableForPlan.getColumnModel().getColumn(j).setCellRenderer(tableCellRenderer1);
                    }

                    MainView.this.tableForPlan.getColumnModel().getColumn(0).setPreferredWidth(200);
                    MainView.this.tableForPlan.setCellSelectionEnabled(false);
                }
            }
        });

        JPanel panelAfterPanelWithTabbed = new JPanel();
        panelWithAllInfo.add(panelAfterPanelWithTabbed,
            new GridConstraints(2, 0, 1, 1,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                null, new Dimension(200, 200), null, 0, false));


        setTitle("Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //add(scrollPaneForTableForPlane);
        //add(scrollPaneForTableForTasks);
        setSize(1000, 400);
        setContentPane(panelWithAllInfo);
        setVisible(true);
    }


}

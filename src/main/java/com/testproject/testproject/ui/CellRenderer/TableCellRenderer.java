package com.testproject.testproject.ui.CellRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class TableCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, value,
            isSelected, hasFocus, row, column);

        if (value != null) {
            //String cellData = value.toString();
            if (value.equals(" "))
                cell.setBackground(Color.LIGHT_GRAY);
            else
                cell.setBackground(Color.WHITE);

        } else
            cell.setBackground(Color.WHITE);
        return cell;
    }
}

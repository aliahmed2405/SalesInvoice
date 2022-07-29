/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sig.model;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author DELL
 */
public class InvoiceLineTableModel extends AbstractTableModel {

    private ArrayList<InvoiceLine> linesArray;
    private String[] cols = {"Item Name", "Unit Price", "Count", "Line Total"};

    public InvoiceLineTableModel(ArrayList<InvoiceLine> linesArray) {
        this.linesArray = linesArray;
    }

    @Override
    public int getRowCount() {
        return linesArray == null ? 0 : linesArray.size();
    }

    @Override
    public int getColumnCount() {
        return cols.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (linesArray == null) {
            return "";
        } else {
            InvoiceLine data = linesArray.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return data.getItem();
                case 1:
                    return data.getPrice();
                case 2:
                    return data.getCount();
                case 3:
                    return data.getLineTotal();
                default:
                    return "";
            }
        }
    }

    @Override
    public String getColumnName(int column) {
        return cols[column];
    }

}

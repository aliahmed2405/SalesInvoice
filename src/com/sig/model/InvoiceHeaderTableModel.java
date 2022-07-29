/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sig.model;

import com.sig.view.InvoiceFrame;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author DELL
 */
public class InvoiceHeaderTableModel extends AbstractTableModel {

    private ArrayList<InvoiceHeader> invArray;
    private String[] cols = {"Invoice Num", "Invoice Date", "Customer Name", "Invoice Total"};
    
    public InvoiceHeaderTableModel(ArrayList<InvoiceHeader> invoicesArray) {
        this.invArray = invoicesArray;
    }

    @Override
    public int getRowCount() {
        return invArray.size();
    }

    @Override
    public int getColumnCount() {
        return cols.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        InvoiceHeader data = invArray.get(rowIndex);
        switch (columnIndex) {
            case 0: return data.getNum();
            case 1: return InvoiceFrame.dateFormat.format(data.getInvDate());
            case 2: return data.getCustomer();
            case 3: return data.getInvoiceTotal();
        }
        return "";
    }

    @Override
    public String getColumnName(int column) {
        return cols[column];
    }
}

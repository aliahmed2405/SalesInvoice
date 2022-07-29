/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sig.controller;

import com.sig.model.InvoiceHeader;
import com.sig.model.InvoiceLine;
import com.sig.model.InvoiceLineTableModel;
import com.sig.view.InvoiceFrame;
import java.util.ArrayList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author DELL
 */
public class TableSelectionListener implements ListSelectionListener {

    private InvoiceFrame form;

    public TableSelectionListener(InvoiceFrame frame) {
        this.form = frame;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        int selectedIndex = form.getInvHTbl().getSelectedRow();
        System.out.println("Invoice selected: " + selectedIndex);
        if (selectedIndex != -1) {
            InvoiceHeader selectedInvoice = form.getInvoicesArray().get(selectedIndex);
            ArrayList<InvoiceLine> lines = selectedInvoice.getLines();
            InvoiceLineTableModel lineTableModel = new InvoiceLineTableModel(lines);
            form.setLinesArray(lines);
            form.getInvLTbl().setModel(lineTableModel);
            form.getCustNameLbl().setText(selectedInvoice.getCustomer());
            form.getInvNumLbl().setText("" + selectedInvoice.getNum());
            form.getInvTotalIbl().setText("" + selectedInvoice.getInvoiceTotal());
            form.getInvDateLbl().setText(InvoiceFrame.dateFormat.format(selectedInvoice.getInvDate()));
        }
    }

}

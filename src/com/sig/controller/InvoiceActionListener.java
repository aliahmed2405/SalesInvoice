/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sig.controller;

import com.sig.model.InvoiceHeader;
import com.sig.model.InvoiceHeaderTableModel;
import com.sig.model.InvoiceLine;
import com.sig.model.InvoiceLineTableModel;
import com.sig.view.InvoiceFrame;
import com.sig.view.InvoiceHeaderDialog;
import com.sig.view.InvoiceLineDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author DELL
 */
public class InvoiceActionListener implements ActionListener {

    private InvoiceFrame form;
    private InvoiceHeaderDialog headerDialog;
    private InvoiceLineDialog lineDialog;

    public InvoiceActionListener(InvoiceFrame form) {
        this.form = form;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Save Files":
                saveFiles();
                break;

            case "Delete Invoice":
                deleteInvoice();
                break;
                
            case "New Invoice":
                createNewInvoice();
                break;
            
            case "Load Files":
                loadFiles();
                break;

            case "New Line":
                createNewLine();
                break;
           
            case "Delete Line":
                deleteLine();
                break;
                
            case "newLineOK":
                newLineDialogOK();
                break;
                
            case "newInvoiceCancel":
                newInvoiceDialogCancel();
                break;

            case "newInvoiceOK":
                newInvoiceDialogOK();
                break;

            case "newLineCancel":
                newLineDialogCancel();
                break;

            
        }
    }

    private void loadFiles() {
        JFileChooser fileChooser = new JFileChooser();
        try {
            int result = fileChooser.showOpenDialog(form);
            if (result == JFileChooser.APPROVE_OPTION) {
                File hF = fileChooser.getSelectedFile();
                Path hP = Paths.get(hF.getAbsolutePath());
                List<String> headerData = Files.readAllLines(hP);
                ArrayList<InvoiceHeader> invoiceHeaders = new ArrayList<>();
                for (String data : headerData) {
                    String[] arr = data.split(",");
                    String str1 = arr[0];
                    String str2 = arr[1];
                    String str3 = arr[2];
                    int code = Integer.parseInt(str1);
                    Date invoiceDate = InvoiceFrame.dateFormat.parse(str2);
                    InvoiceHeader header = new InvoiceHeader(code, str3, invoiceDate);
                    invoiceHeaders.add(header);
                }
                form.setInvoicesArray(invoiceHeaders);

                result = fileChooser.showOpenDialog(form);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File lF = fileChooser.getSelectedFile();
                    Path lP = Paths.get(lF.getAbsolutePath());
                    List<String> linesData = Files.readAllLines(lP);
                    ArrayList<InvoiceLine> invoiceLines = new ArrayList<>();
                    for (String Data : linesData) {
                        String[] arr = Data.split(",");
                        String str1 = arr[0];    // invoice num (int)
                        String str2 = arr[1];    // item name   (String)
                        String str3 = arr[2];    // price       (double)
                        String str4 = arr[3];    // count       (int)
                        int invCode = Integer.parseInt(str1);
                        double price = Double.parseDouble(str3);
                        int count = Integer.parseInt(str4);
                        InvoiceHeader inv = form.getInvObject(invCode);
                        InvoiceLine line = new InvoiceLine(str2, price, count, inv);
                        inv.getLines().add(line);
                    }
                }
                InvoiceHeaderTableModel headerTableModel = new InvoiceHeaderTableModel(invoiceHeaders);
                form.setHeaderTableModel(headerTableModel);
                form.getInvHTbl().setModel(headerTableModel);
                System.out.println("files read");
            }

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(form, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(form, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void saveFiles() {
        ArrayList<InvoiceHeader> invoicesArray = form.getInvoicesArray();
        JFileChooser fc = new JFileChooser();
        try {
            int result = fc.showSaveDialog(form);
            if (result == JFileChooser.APPROVE_OPTION) {
                File headerFile = fc.getSelectedFile();
                FileWriter hfw = new FileWriter(headerFile);
                String headers = "";
                String lines = "";
                for (InvoiceHeader invoice : invoicesArray) {
                    headers += invoice.toString();
                    headers += "\n";
                    for (InvoiceLine line : invoice.getLines()) {
                        lines += line.toString();
                        lines += "\n";
                    }
                }
                //  w e l c o m e
                //  0 1 2 3 4 5 6
                //  1 2 3 4 5 6 7
                headers = headers.substring(0, headers.length()-1);
                lines = lines.substring(0, lines.length()-1);
                result = fc.showSaveDialog(form);
                File lineFile = fc.getSelectedFile();
                FileWriter lfw = new FileWriter(lineFile);
                hfw.write(headers);
                lfw.write(lines);
                hfw.close();
                lfw.close();
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(form, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createNewInvoice() {
        headerDialog = new InvoiceHeaderDialog(form);
        headerDialog.setVisible(true);
    }
        private void deleteInvoice() {
        int selectedInvoice = form.getInvHTbl().getSelectedRow();
        if (selectedInvoice != -1) {
            form.getInvoicesArray().remove(selectedInvoice);
            form.getHeaderTableModel().fireTableDataChanged();

            form.getInvLTbl().setModel(new InvoiceLineTableModel(null));
            form.setLinesArray(null);
            form.getCustNameLbl().setText("");
            form.getInvNumLbl().setText("");
            form.getInvTotalIbl().setText("");
            form.getInvDateLbl().setText("");
        }
    }


    private void createNewLine() {
        lineDialog = new InvoiceLineDialog(form);
        lineDialog.setVisible(true);
    }
    

    private void deleteLine() {
        int selectedLineIndex = form.getInvLTbl().getSelectedRow();
        int selectedInvoiceIndex = form.getInvHTbl().getSelectedRow();
        if (selectedLineIndex != -1) {
            form.getLinesArray().remove(selectedLineIndex);
            InvoiceLineTableModel lineTableModel = (InvoiceLineTableModel) form.getInvLTbl().getModel();
            lineTableModel.fireTableDataChanged();
            form.getInvTotalIbl().setText("" + form.getInvoicesArray().get(selectedInvoiceIndex).getInvoiceTotal());
            form.getHeaderTableModel().fireTableDataChanged();
            form.getInvHTbl().setRowSelectionInterval(selectedInvoiceIndex, selectedInvoiceIndex);
        }
    }

   

    private void newInvoiceDialogCancel() {
        headerDialog.setVisible(false);
        headerDialog.dispose();
        headerDialog = null;
    }

    private void newInvoiceDialogOK() {
        headerDialog.setVisible(false);

        String custName = headerDialog.getCustNameField().getText();
        String str = headerDialog.getInvDateField().getText();
        Date d = new Date();
        try {
            d = InvoiceFrame.dateFormat.parse(str);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(form, "Cannot parse date, resetting to today.", "Invalid date format", JOptionPane.ERROR_MESSAGE);
        }

        int invNum = 0;
        for (InvoiceHeader inv : form.getInvoicesArray()) {
            if (inv.getNum() > invNum) {
                invNum = inv.getNum();
            }
        }
        invNum++;
        InvoiceHeader newInv = new InvoiceHeader(invNum, custName, d);
        form.getInvoicesArray().add(newInv);
        form.getHeaderTableModel().fireTableDataChanged();
        headerDialog.dispose();
        headerDialog = null;
    }

    private void newLineDialogCancel() {
        lineDialog.setVisible(false);
        lineDialog.dispose();
        lineDialog = null;
    }

    private void newLineDialogOK() {
        lineDialog.setVisible(false);

        String name = lineDialog.getItemNameField().getText();
        String str1 = lineDialog.getItemCountField().getText();
        String str2 = lineDialog.getItemPriceField().getText();
        int count = 1;
        double price = 1;
        try {
            count = Integer.parseInt(str1);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(form, "Cannot convert number", "Invalid number format", JOptionPane.ERROR_MESSAGE);
        }

        try {
            price = Double.parseDouble(str2);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(form, "Cannot convert price", "Invalid number format", JOptionPane.ERROR_MESSAGE);
        }
        int selectedInvHeader = form.getInvHTbl().getSelectedRow();
        if (selectedInvHeader != -1) {
            InvoiceHeader invHeader = form.getInvoicesArray().get(selectedInvHeader);
            InvoiceLine line = new InvoiceLine(name, price, count, invHeader);
            //invHeader.getLines().add(line);
            form.getLinesArray().add(line);
            InvoiceLineTableModel lineTableModel = (InvoiceLineTableModel) form.getInvLTbl().getModel();
            lineTableModel.fireTableDataChanged();
            form.getHeaderTableModel().fireTableDataChanged();
        }
        form.getInvHTbl().setRowSelectionInterval(selectedInvHeader, selectedInvHeader);
        lineDialog.dispose();
        lineDialog = null;
    }

}

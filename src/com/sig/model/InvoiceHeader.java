
package com.sig.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class InvoiceHeader {
    private String customer;
    private int num;
    private Date invDate;    
    private DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
    private ArrayList<InvoiceLine> lines;

    public InvoiceHeader(int num, String customer, Date invDate) {
        this.num = num;
        this.customer = customer;
        this.invDate = invDate;
    }
    
    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }
     
    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public Date getInvDate() {
        return invDate;
    }

    public void setInvDate(Date invDate) {
        this.invDate = invDate;
    }

    public ArrayList<InvoiceLine> getLines() {
        if (lines == null) {
            lines = new ArrayList<>();
        }
        return lines;
    }

    public void setLines(ArrayList<InvoiceLine> lines) {
        this.lines = lines;
    }
    
    public double getInvoiceTotal() {
        double total = 0.0;
        
        for (int i = 0; i < getLines().size(); i++) {
            total += getLines().get(i).getLineTotal();
        }
        
        return total;
    }

    @Override
    public String toString() {
        return num + "," + format.format(invDate) + "," + customer;
    }
    
}

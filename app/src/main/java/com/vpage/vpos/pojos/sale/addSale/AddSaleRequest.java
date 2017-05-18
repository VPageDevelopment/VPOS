package com.vpage.vpos.pojos.sale.addSale;

public class AddSaleRequest {

    private String invoice;

    private String item_fk;

    private String customer_fk;

    private String type;

    private String amount_due;

    private String change_due;

    private String amount_tendered;

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public String getItem_fk() {
        return item_fk;
    }

    public void setItem_fk(String item_fk) {
        this.item_fk = item_fk;
    }

    public String getCustomer_fk() {
        return customer_fk;
    }

    public void setCustomer_fk(String customer_fk) {
        this.customer_fk = customer_fk;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAmount_due() {
        return amount_due;
    }

    public void setAmount_due(String amount_due) {
        this.amount_due = amount_due;
    }

    public String getChange_due() {
        return change_due;
    }

    public void setChange_due(String change_due) {
        this.change_due = change_due;
    }

    public String getAmount_tendered() {
        return amount_tendered;
    }

    public void setAmount_tendered(String amount_tendered) {
        this.amount_tendered = amount_tendered;
    }

    @Override
    public String toString() {
        return "AddSaleRequest{" +
                "invoice='" + invoice + '\'' +
                ", item_fk='" + item_fk + '\'' +
                ", customer_fk='" + customer_fk + '\'' +
                ", type='" + type + '\'' +
                ", amount_due='" + amount_due + '\'' +
                ", change_due='" + change_due + '\'' +
                ", amount_tendered='" + amount_tendered + '\'' +
                '}';
    }
}

package com.vpage.vpos.pojos.storeConfigInvoice;


public class UpdateStoreConfigInvoiceRequest {

    private String invoice_email_template;

    private String enable_invoice;

    private String default_invoice_comments;

    private String receiving_invoice_formate;

    private String sales_invoice_formate;

    public String getInvoice_email_template() {
        return invoice_email_template;
    }

    public void setInvoice_email_template(String invoice_email_template) {
        this.invoice_email_template = invoice_email_template;
    }

    public String getEnable_invoice() {
        return enable_invoice;
    }

    public void setEnable_invoice(String enable_invoice) {
        this.enable_invoice = enable_invoice;
    }

    public String getDefault_invoice_comments() {
        return default_invoice_comments;
    }

    public void setDefault_invoice_comments(String default_invoice_comments) {
        this.default_invoice_comments = default_invoice_comments;
    }

    public String getReceiving_invoice_formate() {
        return receiving_invoice_formate;
    }

    public void setReceiving_invoice_formate(String receiving_invoice_formate) {
        this.receiving_invoice_formate = receiving_invoice_formate;
    }

    public String getSales_invoice_formate() {
        return sales_invoice_formate;
    }

    public void setSales_invoice_formate(String sales_invoice_formate) {
        this.sales_invoice_formate = sales_invoice_formate;
    }

    @Override
    public String toString() {
        return "UpdateStoreConfigInvoiceRequest{" +
                "invoice_email_template='" + invoice_email_template + '\'' +
                ", enable_invoice='" + enable_invoice + '\'' +
                ", default_invoice_comments='" + default_invoice_comments + '\'' +
                ", receiving_invoice_formate='" + receiving_invoice_formate + '\'' +
                ", sales_invoice_formate='" + sales_invoice_formate + '\'' +
                '}';
    }
}

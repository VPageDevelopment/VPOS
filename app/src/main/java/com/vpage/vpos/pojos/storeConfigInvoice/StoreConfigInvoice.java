package com.vpage.vpos.pojos.storeConfigInvoice;


public class StoreConfigInvoice {

    private String invoice_email_template;

    private String updated_at;

    private String enable_invoice;

    private String created_at;

    private String default_invoice_comments;

    private String sc_invoice_id;

    private String receiving_invoice_formate;

    private String sales_invoice_formate;

    public String getInvoice_email_template() {
        return invoice_email_template;
    }

    public void setInvoice_email_template(String invoice_email_template) {
        this.invoice_email_template = invoice_email_template;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getEnable_invoice() {
        return enable_invoice;
    }

    public void setEnable_invoice(String enable_invoice) {
        this.enable_invoice = enable_invoice;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getDefault_invoice_comments() {
        return default_invoice_comments;
    }

    public void setDefault_invoice_comments(String default_invoice_comments) {
        this.default_invoice_comments = default_invoice_comments;
    }

    public String getSc_invoice_id() {
        return sc_invoice_id;
    }

    public void setSc_invoice_id(String sc_invoice_id) {
        this.sc_invoice_id = sc_invoice_id;
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
        return "StoreConfigInvoice{" +
                "invoice_email_template='" + invoice_email_template + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", enable_invoice='" + enable_invoice + '\'' +
                ", created_at='" + created_at + '\'' +
                ", default_invoice_comments='" + default_invoice_comments + '\'' +
                ", sc_invoice_id='" + sc_invoice_id + '\'' +
                ", receiving_invoice_formate='" + receiving_invoice_formate + '\'' +
                ", sales_invoice_formate='" + sales_invoice_formate + '\'' +
                '}';
    }
}

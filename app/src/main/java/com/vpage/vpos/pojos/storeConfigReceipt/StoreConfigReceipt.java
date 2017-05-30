package com.vpage.vpos.pojos.storeConfigReceipt;


public class StoreConfigReceipt {

    private String receipt_template;

    private String show_taxes;

    private String invoice_printer;

    private String margin_bottom;

    private String takings_printer;

    private String ticket_printer;

    private String print_browser_header;

    private String sc_receipt_id;

    private String show_sno;

    private String margin_top;

    private String updated_at;

    private String margin_right;

    private String margin_left;

    private String created_at;

    private String show_print_dialog;

    private String show_desc;

    private String print_browser_footer;

    public String getReceipt_template() {
        return receipt_template;
    }

    public void setReceipt_template(String receipt_template) {
        this.receipt_template = receipt_template;
    }

    public String getShow_taxes() {
        return show_taxes;
    }

    public void setShow_taxes(String show_taxes) {
        this.show_taxes = show_taxes;
    }

    public String getInvoice_printer() {
        return invoice_printer;
    }

    public void setInvoice_printer(String invoice_printer) {
        this.invoice_printer = invoice_printer;
    }

    public String getMargin_bottom() {
        return margin_bottom;
    }

    public void setMargin_bottom(String margin_bottom) {
        this.margin_bottom = margin_bottom;
    }

    public String getTakings_printer() {
        return takings_printer;
    }

    public void setTakings_printer(String takings_printer) {
        this.takings_printer = takings_printer;
    }

    public String getTicket_printer() {
        return ticket_printer;
    }

    public void setTicket_printer(String ticket_printer) {
        this.ticket_printer = ticket_printer;
    }

    public String getPrint_browser_header() {
        return print_browser_header;
    }

    public void setPrint_browser_header(String print_browser_header) {
        this.print_browser_header = print_browser_header;
    }

    public String getSc_receipt_id() {
        return sc_receipt_id;
    }

    public void setSc_receipt_id(String sc_receipt_id) {
        this.sc_receipt_id = sc_receipt_id;
    }

    public String getShow_sno() {
        return show_sno;
    }

    public void setShow_sno(String show_sno) {
        this.show_sno = show_sno;
    }

    public String getMargin_top() {
        return margin_top;
    }

    public void setMargin_top(String margin_top) {
        this.margin_top = margin_top;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getMargin_right() {
        return margin_right;
    }

    public void setMargin_right(String margin_right) {
        this.margin_right = margin_right;
    }

    public String getMargin_left() {
        return margin_left;
    }

    public void setMargin_left(String margin_left) {
        this.margin_left = margin_left;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getShow_print_dialog() {
        return show_print_dialog;
    }

    public void setShow_print_dialog(String show_print_dialog) {
        this.show_print_dialog = show_print_dialog;
    }

    public String getShow_desc() {
        return show_desc;
    }

    public void setShow_desc(String show_desc) {
        this.show_desc = show_desc;
    }

    public String getPrint_browser_footer() {
        return print_browser_footer;
    }

    public void setPrint_browser_footer(String print_browser_footer) {
        this.print_browser_footer = print_browser_footer;
    }

    @Override
    public String toString() {
        return "StoreConfigReceipt{" +
                "receipt_template='" + receipt_template + '\'' +
                ", show_taxes='" + show_taxes + '\'' +
                ", invoice_printer='" + invoice_printer + '\'' +
                ", margin_bottom='" + margin_bottom + '\'' +
                ", takings_printer='" + takings_printer + '\'' +
                ", ticket_printer='" + ticket_printer + '\'' +
                ", print_browser_header='" + print_browser_header + '\'' +
                ", sc_receipt_id='" + sc_receipt_id + '\'' +
                ", show_sno='" + show_sno + '\'' +
                ", margin_top='" + margin_top + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", margin_right='" + margin_right + '\'' +
                ", margin_left='" + margin_left + '\'' +
                ", created_at='" + created_at + '\'' +
                ", show_print_dialog='" + show_print_dialog + '\'' +
                ", show_desc='" + show_desc + '\'' +
                ", print_browser_footer='" + print_browser_footer + '\'' +
                '}';
    }
}

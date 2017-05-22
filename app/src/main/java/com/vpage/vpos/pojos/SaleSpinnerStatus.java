package com.vpage.vpos.pojos;


public class SaleSpinnerStatus {

    private boolean idStatus;
    private boolean timeStatus;
    private boolean customerStatus;
    private boolean amountDueStatus;
    private boolean amountTendStatus;
    private boolean changeDueStatus;
    private boolean typeStatus;
    private boolean invoiceStatus;

    public boolean isIdStatus() {
        return idStatus;
    }

    public void setIdStatus(boolean idStatus) {
        this.idStatus = idStatus;
    }

    public boolean isTimeStatus() {
        return timeStatus;
    }

    public void setTimeStatus(boolean timeStatus) {
        this.timeStatus = timeStatus;
    }

    public boolean isCustomerStatus() {
        return customerStatus;
    }

    public void setCustomerStatus(boolean customerStatus) {
        this.customerStatus = customerStatus;
    }

    public boolean isAmountDueStatus() {
        return amountDueStatus;
    }

    public void setAmountDueStatus(boolean amountDueStatus) {
        this.amountDueStatus = amountDueStatus;
    }

    public boolean isAmountTendStatus() {
        return amountTendStatus;
    }

    public void setAmountTendStatus(boolean amountTendStatus) {
        this.amountTendStatus = amountTendStatus;
    }

    public boolean isChangeDueStatus() {
        return changeDueStatus;
    }

    public void setChangeDueStatus(boolean changeDueStatus) {
        this.changeDueStatus = changeDueStatus;
    }

    public boolean isTypeStatus() {
        return typeStatus;
    }

    public void setTypeStatus(boolean typeStatus) {
        this.typeStatus = typeStatus;
    }

    public boolean isInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(boolean invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }
}

package com.vpage.vpos.pojos;


public class CustomerSpinnerStatus {

    private boolean idStatus;
    private boolean fNameStatus;
    private boolean lNameStatus;
    private boolean emailStatus;
    private boolean phoneNoStatus;

    public boolean isIdStatus() {
        return idStatus;
    }

    public void setIdStatus(boolean idStatus) {
        this.idStatus = idStatus;
    }

    public boolean isfNameStatus() {
        return fNameStatus;
    }

    public void setfNameStatus(boolean fNameStatus) {
        this.fNameStatus = fNameStatus;
    }

    public boolean islNameStatus() {
        return lNameStatus;
    }

    public void setlNameStatus(boolean lNameStatus) {
        this.lNameStatus = lNameStatus;
    }

    public boolean isEmailStatus() {
        return emailStatus;
    }

    public void setEmailStatus(boolean emailStatus) {
        this.emailStatus = emailStatus;
    }

    public boolean isPhoneNoStatus() {
        return phoneNoStatus;
    }

    public void setPhoneNoStatus(boolean phoneNoStatus) {
        this.phoneNoStatus = phoneNoStatus;
    }

    @Override
    public String toString() {
        return "CustomerSpinnerStatus{" +
                "idStatus=" + idStatus +
                ", fNameStatus=" + fNameStatus +
                ", lNameStatus=" + lNameStatus +
                ", emailStatus=" + emailStatus +
                ", phoneNoStatus=" + phoneNoStatus +
                '}';
    }
}

package com.vpage.vpos.pojos;

public class SupplierSpinnerStatus {

    private boolean idStatus;
    private boolean cNameStatus;
    private boolean aNameStatus;
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

    public boolean iscNameStatus() {
        return cNameStatus;
    }

    public void setcNameStatus(boolean cNameStatus) {
        this.cNameStatus = cNameStatus;
    }

    public boolean isaNameStatus() {
        return aNameStatus;
    }

    public void setaNameStatus(boolean aNameStatus) {
        this.aNameStatus = aNameStatus;
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
        return "SupplierSpinnerStatus{" +
                "idStatus=" + idStatus +
                ", cNameStatus=" + cNameStatus +
                ", aNameStatus=" + aNameStatus +
                ", fNameStatus=" + fNameStatus +
                ", lNameStatus=" + lNameStatus +
                ", emailStatus=" + emailStatus +
                ", phoneNoStatus=" + phoneNoStatus +
                '}';
    }
}

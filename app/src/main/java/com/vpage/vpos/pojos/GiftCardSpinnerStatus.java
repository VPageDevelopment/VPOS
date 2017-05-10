package com.vpage.vpos.pojos;


public class GiftCardSpinnerStatus {

    private boolean idStatus;
    private boolean fNameStatus;
    private boolean lNameStatus;
    private boolean giftCardNoStatus;
    private boolean giftCardValueStatus;

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

    public boolean isGiftCardNoStatus() {
        return giftCardNoStatus;
    }

    public void setGiftCardNoStatus(boolean giftCardNoStatus) {
        this.giftCardNoStatus = giftCardNoStatus;
    }

    public boolean isGiftCardValueStatus() {
        return giftCardValueStatus;
    }

    public void setGiftCardValueStatus(boolean giftCardValueStatus) {
        this.giftCardValueStatus = giftCardValueStatus;
    }

    @Override
    public String toString() {
        return "GiftCardSpinnerStatus{" +
                "idStatus=" + idStatus +
                ", fNameStatus=" + fNameStatus +
                ", lNameStatus=" + lNameStatus +
                ", giftCardNoStatus=" + giftCardNoStatus +
                ", giftCardValueStatus=" + giftCardValueStatus +
                '}';
    }
}

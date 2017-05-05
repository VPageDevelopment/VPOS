package com.vpage.vpos.pojos;

public class ItemKitSpinnerStatus {

    private boolean idStatus;
    private boolean itemKitNameStatus;
    private boolean itemKitDesStatus;
    private boolean costPriceStatus;
    private boolean retailPriceStatus;

    public boolean isIdStatus() {
        return idStatus;
    }

    public void setIdStatus(boolean idStatus) {
        this.idStatus = idStatus;
    }

    public boolean isItemKitNameStatus() {
        return itemKitNameStatus;
    }

    public void setItemKitNameStatus(boolean itemKitNameStatus) {
        this.itemKitNameStatus = itemKitNameStatus;
    }

    public boolean isItemKitDesStatus() {
        return itemKitDesStatus;
    }

    public void setItemKitDesStatus(boolean itemKitDesStatus) {
        this.itemKitDesStatus = itemKitDesStatus;
    }

    public boolean isCostPriceStatus() {
        return costPriceStatus;
    }

    public void setCostPriceStatus(boolean costPriceStatus) {
        this.costPriceStatus = costPriceStatus;
    }

    public boolean isRetailPriceStatus() {
        return retailPriceStatus;
    }

    public void setRetailPriceStatus(boolean retailPriceStatus) {
        this.retailPriceStatus = retailPriceStatus;
    }

    @Override
    public String toString() {
        return "ItemKitSpinnerStatus{" +
                "idStatus=" + idStatus +
                ", itemKitNameStatus=" + itemKitNameStatus +
                ", itemKitDesStatus=" + itemKitDesStatus +
                ", costPriceStatus=" + costPriceStatus +
                ", retailPriceStatus=" + retailPriceStatus +
                '}';
    }
}

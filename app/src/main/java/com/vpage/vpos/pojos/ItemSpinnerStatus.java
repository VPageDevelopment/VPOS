package com.vpage.vpos.pojos;

public class ItemSpinnerStatus {

    private boolean idStatus;
    private boolean barCodeStatus;
    private boolean itemNameStatus;
    private boolean categoryStatus;
    private boolean companyNameStatus;
    private boolean costPriceStatus;
    private boolean retailPriceStatus;
    private boolean quantityStatus;
    private boolean taxPercentStatus;
    private boolean avatarStatus;

    public boolean isIdStatus() {
        return idStatus;
    }

    public void setIdStatus(boolean idStatus) {
        this.idStatus = idStatus;
    }

    public boolean isBarCodeStatus() {
        return barCodeStatus;
    }

    public void setBarCodeStatus(boolean barCodeStatus) {
        this.barCodeStatus = barCodeStatus;
    }

    public boolean isItemNameStatus() {
        return itemNameStatus;
    }

    public void setItemNameStatus(boolean itemNameStatus) {
        this.itemNameStatus = itemNameStatus;
    }

    public boolean isCategoryStatus() {
        return categoryStatus;
    }

    public void setCategoryStatus(boolean categoryStatus) {
        this.categoryStatus = categoryStatus;
    }

    public boolean isCompanyNameStatus() {
        return companyNameStatus;
    }

    public void setCompanyNameStatus(boolean companyNameStatus) {
        this.companyNameStatus = companyNameStatus;
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

    public boolean isQuantityStatus() {
        return quantityStatus;
    }

    public void setQuantityStatus(boolean quantityStatus) {
        this.quantityStatus = quantityStatus;
    }

    public boolean isTaxPercentStatus() {
        return taxPercentStatus;
    }

    public void setTaxPercentStatus(boolean taxPercentStatus) {
        this.taxPercentStatus = taxPercentStatus;
    }

    public boolean isAvatarStatus() {
        return avatarStatus;
    }

    public void setAvatarStatus(boolean avatarStatus) {
        this.avatarStatus = avatarStatus;
    }

    @Override
    public String toString() {
        return "ItemSpinnerStatus{" +
                "idStatus=" + idStatus +
                ", barCodeStatus=" + barCodeStatus +
                ", itemNameStatus=" + itemNameStatus +
                ", categoryStatus=" + categoryStatus +
                ", companyNameStatus=" + companyNameStatus +
                ", costPriceStatus=" + costPriceStatus +
                ", retailPriceStatus=" + retailPriceStatus +
                ", quantityStatus=" + quantityStatus +
                ", taxPercentStatus=" + taxPercentStatus +
                ", avatarStatus=" + avatarStatus +
                '}';
    }
}


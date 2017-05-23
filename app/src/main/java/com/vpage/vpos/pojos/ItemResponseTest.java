package com.vpage.vpos.pojos;

public class ItemResponseTest {

    private String Id;
    private String barcode;
    private String itemName;
    private String category;
    private String companyName;
    private String costPrice;
    private String retailPrice;
    private String quantity;
    private String taxPercent;
    private String avatarUrl;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(String costPrice) {
        this.costPrice = costPrice;
    }

    public String getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(String retailPrice) {
        this.retailPrice = retailPrice;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTaxPercent() {
        return taxPercent;
    }

    public void setTaxPercent(String taxPercent) {
        this.taxPercent = taxPercent;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    @Override
    public String toString() {
        return "ItemResponse{" +
                "Id='" + Id + '\'' +
                ", barcode='" + barcode + '\'' +
                ", itemName='" + itemName + '\'' +
                ", category='" + category + '\'' +
                ", companyName='" + companyName + '\'' +
                ", costPrice='" + costPrice + '\'' +
                ", retailPrice='" + retailPrice + '\'' +
                ", quantity='" + quantity + '\'' +
                ", taxPercent='" + taxPercent + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                '}';
    }
}

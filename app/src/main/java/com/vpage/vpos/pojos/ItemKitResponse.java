package com.vpage.vpos.pojos;

public class ItemKitResponse {

    private String Id;
    private String itemKitName;
    private String itemKitDescription;
    private String costPrice;
    private String retailPrice;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getItemKitName() {
        return itemKitName;
    }

    public void setItemKitName(String itemKitName) {
        this.itemKitName = itemKitName;
    }

    public String getItemKitDescription() {
        return itemKitDescription;
    }

    public void setItemKitDescription(String itemKitDescription) {
        this.itemKitDescription = itemKitDescription;
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

    @Override
    public String toString() {
        return "ItemKitResponse{" +
                "Id='" + Id + '\'' +
                ", itemKitName='" + itemKitName + '\'' +
                ", itemKitDescription='" + itemKitDescription + '\'' +
                ", costPrice='" + costPrice + '\'' +
                ", retailPrice='" + retailPrice + '\'' +
                '}';
    }
}

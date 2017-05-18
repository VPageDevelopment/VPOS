package com.vpage.vpos.pojos.itemkits.addItemKits;

public class AddItemKitsRequest {

    private String item_fk;

    private String item_kit_desc;

    private String item_kit_name;

    public String getItem_fk() {
        return item_fk;
    }

    public void setItem_fk(String item_fk) {
        this.item_fk = item_fk;
    }

    public String getItem_kit_desc() {
        return item_kit_desc;
    }

    public void setItem_kit_desc(String item_kit_desc) {
        this.item_kit_desc = item_kit_desc;
    }

    public String getItem_kit_name() {
        return item_kit_name;
    }

    public void setItem_kit_name(String item_kit_name) {
        this.item_kit_name = item_kit_name;
    }

    @Override
    public String toString() {
        return "AddItemKitsRequest{" +
                "item_fk='" + item_fk + '\'' +
                ", item_kit_desc='" + item_kit_desc + '\'' +
                ", item_kit_name='" + item_kit_name + '\'' +
                '}';
    }
}

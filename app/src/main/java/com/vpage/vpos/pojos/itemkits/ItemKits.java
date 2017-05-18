package com.vpage.vpos.pojos.itemkits;


public class ItemKits {

    private String update_at;

    private String created_at;

    private String item_fk;

    private String item_kit_id;

    private String item_kit_desc;

    private String item_kit_name;

    public String getUpdate_at() {
        return update_at;
    }

    public void setUpdate_at(String update_at) {
        this.update_at = update_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getItem_fk() {
        return item_fk;
    }

    public void setItem_fk(String item_fk) {
        this.item_fk = item_fk;
    }

    public String getItem_kit_id() {
        return item_kit_id;
    }

    public void setItem_kit_id(String item_kit_id) {
        this.item_kit_id = item_kit_id;
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
        return "ItemKits{" +
                "update_at='" + update_at + '\'' +
                ", created_at='" + created_at + '\'' +
                ", item_fk='" + item_fk + '\'' +
                ", item_kit_id='" + item_kit_id + '\'' +
                ", item_kit_desc='" + item_kit_desc + '\'' +
                ", item_kit_name='" + item_kit_name + '\'' +
                '}';
    }
}

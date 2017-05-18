package com.vpage.vpos.pojos.item;

public class Items {

    private String quantity_stock;

    private String receiving_quantity;

    private String cost_price;

    private String tax_two;

    private String upc_ean_isbn;

    private String retail_price;

    private String avatar;

    private String deleted;

    private String allow_alt_description;

    private String category;

    private String supplier_fk;

    private String tax_one;

    private String updated_at;

    private String item_name;

    private String item_id;

    private String description;

    private String created_at;

    private String item_has_serial_number;

    public String getItem_has_serial_number() {
        return item_has_serial_number;
    }

    public void setItem_has_serial_number(String item_has_serial_number) {
        this.item_has_serial_number = item_has_serial_number;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getTax_one() {
        return tax_one;
    }

    public void setTax_one(String tax_one) {
        this.tax_one = tax_one;
    }

    public String getSupplier_fk() {
        return supplier_fk;
    }

    public void setSupplier_fk(String supplier_fk) {
        this.supplier_fk = supplier_fk;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAllow_alt_description() {
        return allow_alt_description;
    }

    public void setAllow_alt_description(String allow_alt_description) {
        this.allow_alt_description = allow_alt_description;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getRetail_price() {
        return retail_price;
    }

    public void setRetail_price(String retail_price) {
        this.retail_price = retail_price;
    }

    public String getUpc_ean_isbn() {
        return upc_ean_isbn;
    }

    public void setUpc_ean_isbn(String upc_ean_isbn) {
        this.upc_ean_isbn = upc_ean_isbn;
    }

    public String getTax_two() {
        return tax_two;
    }

    public void setTax_two(String tax_two) {
        this.tax_two = tax_two;
    }

    public String getCost_price() {
        return cost_price;
    }

    public void setCost_price(String cost_price) {
        this.cost_price = cost_price;
    }

    public String getReceiving_quantity() {
        return receiving_quantity;
    }

    public void setReceiving_quantity(String receiving_quantity) {
        this.receiving_quantity = receiving_quantity;
    }

    public String getQuantity_stock() {
        return quantity_stock;
    }

    public void setQuantity_stock(String quantity_stock) {
        this.quantity_stock = quantity_stock;
    }

    @Override
    public String toString() {
        return "Items{" +
                "quantity_stock='" + quantity_stock + '\'' +
                ", receiving_quantity='" + receiving_quantity + '\'' +
                ", cost_price='" + cost_price + '\'' +
                ", tax_two='" + tax_two + '\'' +
                ", upc_ean_isbn='" + upc_ean_isbn + '\'' +
                ", retail_price='" + retail_price + '\'' +
                ", avatar='" + avatar + '\'' +
                ", deleted='" + deleted + '\'' +
                ", allow_alt_description='" + allow_alt_description + '\'' +
                ", category='" + category + '\'' +
                ", supplier_fk='" + supplier_fk + '\'' +
                ", tax_one='" + tax_one + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", item_name='" + item_name + '\'' +
                ", item_id='" + item_id + '\'' +
                ", description='" + description + '\'' +
                ", created_at='" + created_at + '\'' +
                ", item_has_serial_number='" + item_has_serial_number + '\'' +
                '}';
    }
}

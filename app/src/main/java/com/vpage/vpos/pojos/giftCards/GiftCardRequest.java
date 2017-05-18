package com.vpage.vpos.pojos.giftCards;

public class GiftCardRequest {

    private String updated_at;

    private String gift_card_number;

    private String created_at;

    private String gc_value;

    private String gift_card_id;

    private String customer_fk;

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getGift_card_number() {
        return gift_card_number;
    }

    public void setGift_card_number(String gift_card_number) {
        this.gift_card_number = gift_card_number;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getGc_value() {
        return gc_value;
    }

    public void setGc_value(String gc_value) {
        this.gc_value = gc_value;
    }

    public String getGift_card_id() {
        return gift_card_id;
    }

    public void setGift_card_id(String gift_card_id) {
        this.gift_card_id = gift_card_id;
    }

    public String getCustomer_fk() {
        return customer_fk;
    }

    public void setCustomer_fk(String customer_fk) {
        this.customer_fk = customer_fk;
    }

    @Override
    public String toString() {
        return "GiftCardRequest{" +
                "updated_at='" + updated_at + '\'' +
                ", gift_card_number='" + gift_card_number + '\'' +
                ", created_at='" + created_at + '\'' +
                ", gc_value='" + gc_value + '\'' +
                ", gift_card_id='" + gift_card_id + '\'' +
                ", customer_fk='" + customer_fk + '\'' +
                '}';
    }
}

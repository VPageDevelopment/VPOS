package com.vpage.vpos.pojos.giftCards.addGiftCards;

public class AddGiftCardsRequest {

    private String gift_card_number;

    private String gc_value;

    private String customer_fk;

    public String getGift_card_number() {
        return gift_card_number;
    }

    public void setGift_card_number(String gift_card_number) {
        this.gift_card_number = gift_card_number;
    }

    public String getGc_value() {
        return gc_value;
    }

    public void setGc_value(String gc_value) {
        this.gc_value = gc_value;
    }

    public String getCustomer_fk() {
        return customer_fk;
    }

    public void setCustomer_fk(String customer_fk) {
        this.customer_fk = customer_fk;
    }

    @Override
    public String toString() {
        return "AddGiftCardsRequest{" +
                "gift_card_number='" + gift_card_number + '\'' +
                ", gc_value='" + gc_value + '\'' +
                ", customer_fk='" + customer_fk + '\'' +
                '}';
    }
}

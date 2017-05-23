package com.vpage.vpos.pojos.giftCards;

import java.util.Arrays;

public class GiftCardResponse {

    private String status;

    private String status_code;

    private GiftCard[] giftCards;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public GiftCard[] getGiftCards() {
        return giftCards;
    }

    public void setGiftCards(GiftCard[] giftCards) {
        this.giftCards = giftCards;
    }

    @Override
    public String toString() {
        return "GiftCardResponse{" +
                "status='" + status + '\'' +
                ", status_code='" + status_code + '\'' +
                ", giftCards=" + Arrays.toString(giftCards) +
                '}';
    }
}

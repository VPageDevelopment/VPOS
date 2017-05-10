package com.vpage.vpos.pojos;


public class GiftCardResponse {

    private String Id;
    private String firstName;
    private String lastName;
    private int giftCardNumber;
    private float giftCardValue;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getGiftCardNumber() {
        return giftCardNumber;
    }

    public void setGiftCardNumber(int giftCardNumber) {
        this.giftCardNumber = giftCardNumber;
    }

    public float getGiftCardValue() {
        return giftCardValue;
    }

    public void setGiftCardValue(float giftCardValue) {
        this.giftCardValue = giftCardValue;
    }

    @Override
    public String toString() {
        return "GiftCardResponse{" +
                "Id='" + Id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", giftCardNumber='" + giftCardNumber + '\'' +
                ", giftCardValue='" + giftCardValue + '\'' +
                '}';
    }
}

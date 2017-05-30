package com.vpage.vpos.pojos.storeConfigLocal;


public class UpdateStoreConfigLocalRequest {

    private String currency_decimals;

    private String tax_decimals;

    private String quantity_decimals;

    private String thousands_seperator;

    private String country_code;

    private String timezone;

    private String payment_option_order;

    private String time_format;

    private String currency_symbol;

    private String language;

    private String data_format;

    private String localisation;

    public String getCurrency_decimals() {
        return currency_decimals;
    }

    public void setCurrency_decimals(String currency_decimals) {
        this.currency_decimals = currency_decimals;
    }

    public String getTax_decimals() {
        return tax_decimals;
    }

    public void setTax_decimals(String tax_decimals) {
        this.tax_decimals = tax_decimals;
    }

    public String getQuantity_decimals() {
        return quantity_decimals;
    }

    public void setQuantity_decimals(String quantity_decimals) {
        this.quantity_decimals = quantity_decimals;
    }

    public String getThousands_seperator() {
        return thousands_seperator;
    }

    public void setThousands_seperator(String thousands_seperator) {
        this.thousands_seperator = thousands_seperator;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getPayment_option_order() {
        return payment_option_order;
    }

    public void setPayment_option_order(String payment_option_order) {
        this.payment_option_order = payment_option_order;
    }

    public String getTime_format() {
        return time_format;
    }

    public void setTime_format(String time_format) {
        this.time_format = time_format;
    }

    public String getCurrency_symbol() {
        return currency_symbol;
    }

    public void setCurrency_symbol(String currency_symbol) {
        this.currency_symbol = currency_symbol;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getData_format() {
        return data_format;
    }

    public void setData_format(String data_format) {
        this.data_format = data_format;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    @Override
    public String toString() {
        return "UpdateStoreConfigLocalRequest{" +
                "currency_decimals='" + currency_decimals + '\'' +
                ", tax_decimals='" + tax_decimals + '\'' +
                ", quantity_decimals='" + quantity_decimals + '\'' +
                ", thousands_seperator='" + thousands_seperator + '\'' +
                ", country_code='" + country_code + '\'' +
                ", timezone='" + timezone + '\'' +
                ", payment_option_order='" + payment_option_order + '\'' +
                ", time_format='" + time_format + '\'' +
                ", currency_symbol='" + currency_symbol + '\'' +
                ", language='" + language + '\'' +
                ", data_format='" + data_format + '\'' +
                ", localisation='" + localisation + '\'' +
                '}';
    }
}

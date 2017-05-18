package com.vpage.vpos.pojos.supplier.addSupplier;

public class AddSupplierRequest {

    private String zip;

    private String phone_number;

    private String state;

    private String agency_name;

    private String country;

    private String city;

    private String first_name;

    private String company_name;

    private String email;

    private String last_name;

    private String account;

    private String gender;

    private String comments;

    private String address_one;

    private String address_two;

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAgency_name() {
        return agency_name;
    }

    public void setAgency_name(String agency_name) {
        this.agency_name = agency_name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getAddress_one() {
        return address_one;
    }

    public void setAddress_one(String address_one) {
        this.address_one = address_one;
    }

    public String getAddress_two() {
        return address_two;
    }

    public void setAddress_two(String address_two) {
        this.address_two = address_two;
    }

    @Override
    public String toString() {
        return "AddSupplierRequest{" +
                "zip='" + zip + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", state='" + state + '\'' +
                ", agency_name='" + agency_name + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", first_name='" + first_name + '\'' +
                ", company_name='" + company_name + '\'' +
                ", email='" + email + '\'' +
                ", last_name='" + last_name + '\'' +
                ", account='" + account + '\'' +
                ", gender='" + gender + '\'' +
                ", comments='" + comments + '\'' +
                ", address_one='" + address_one + '\'' +
                ", address_two='" + address_two + '\'' +
                '}';
    }
}

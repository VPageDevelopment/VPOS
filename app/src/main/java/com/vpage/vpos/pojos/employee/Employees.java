package com.vpage.vpos.pojos.employee;


public class Employees {

    private String zip;

    private String phone_number;

    private String state;

    private String city;

    private String country;

    private String first_name;

    private String updated_at;

    private String email;

    private String last_name;

    private String created_at;

    private String gender;

    private String employee_id;

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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
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

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(String employee_id) {
        this.employee_id = employee_id;
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
        return "Employees{" +
                "zip='" + zip + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", state='" + state + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", first_name='" + first_name + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", email='" + email + '\'' +
                ", last_name='" + last_name + '\'' +
                ", created_at='" + created_at + '\'' +
                ", gender='" + gender + '\'' +
                ", employee_id='" + employee_id + '\'' +
                ", comments='" + comments + '\'' +
                ", address_one='" + address_one + '\'' +
                ", address_two='" + address_two + '\'' +
                '}';
    }
}

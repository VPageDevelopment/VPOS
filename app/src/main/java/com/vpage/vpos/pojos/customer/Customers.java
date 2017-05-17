package com.vpage.vpos.pojos.customer;

public class Customers
{
    private String total;

    private String zip;

    private String phone_number;

    private String taxable;

    private String state;

    private String discount;

    private String city;

    private String country;

    private String first_name;

    private String updated_at;

    private String email;

    private String company;

    private String last_name;

    private String created_at;

    private String account;

    private String gender;

    private String comments;

    private String customer_id;

    private String address_one;

    private String address_two;

    public String getTotal ()
    {
        return total;
    }

    public void setTotal (String total)
    {
        this.total = total;
    }

    public String getZip ()
    {
        return zip;
    }

    public void setZip (String zip)
    {
        this.zip = zip;
    }

    public String getPhone_number ()
    {
        return phone_number;
    }

    public void setPhone_number (String phone_number)
    {
        this.phone_number = phone_number;
    }

    public String getTaxable ()
    {
        return taxable;
    }

    public void setTaxable (String taxable)
    {
        this.taxable = taxable;
    }

    public String getState ()
    {
        return state;
    }

    public void setState (String state)
    {
        this.state = state;
    }

    public String getDiscount ()
    {
        return discount;
    }

    public void setDiscount (String discount)
    {
        this.discount = discount;
    }

    public String getCity ()
    {
        return city;
    }

    public void setCity (String city)
    {
        this.city = city;
    }

    public String getCountry ()
    {
        return country;
    }

    public void setCountry (String country)
    {
        this.country = country;
    }

    public String getFirst_name ()
    {
        return first_name;
    }

    public void setFirst_name (String first_name)
    {
        this.first_name = first_name;
    }

    public String getUpdated_at ()
    {
        return updated_at;
    }

    public void setUpdated_at (String updated_at)
    {
        this.updated_at = updated_at;
    }

    public String getEmail ()
    {
        return email;
    }

    public void setEmail (String email)
    {
        this.email = email;
    }

    public String getCompany ()
    {
        return company;
    }

    public void setCompany (String company)
    {
        this.company = company;
    }

    public String getLast_name ()
    {
        return last_name;
    }

    public void setLast_name (String last_name)
    {
        this.last_name = last_name;
    }

    public String getCreated_at ()
    {
        return created_at;
    }

    public void setCreated_at (String created_at)
    {
        this.created_at = created_at;
    }

    public String getAccount ()
    {
        return account;
    }

    public void setAccount (String account)
    {
        this.account = account;
    }

    public String getGender ()
    {
        return gender;
    }

    public void setGender (String gender)
    {
        this.gender = gender;
    }

    public String getComments ()
    {
        return comments;
    }

    public void setComments (String comments)
    {
        this.comments = comments;
    }

    public String getCustomer_id ()
    {
        return customer_id;
    }

    public void setCustomer_id (String customer_id)
    {
        this.customer_id = customer_id;
    }

    public String getAddress_one ()
    {
        return address_one;
    }

    public void setAddress_one (String address_one)
    {
        this.address_one = address_one;
    }

    public String getAddress_two ()
    {
        return address_two;
    }

    public void setAddress_two (String address_two)
    {

        this.address_two = address_two;
    }

    @Override
    public String toString()
    {
        return "Customers [total = "+total+", zip = "+zip+", phone_number = "+phone_number+", taxable = "+taxable+", state = "+state+", discount = "+discount+", city = "+city+", country = "+country+", first_name = "+first_name+", updated_at = "+updated_at+", email = "+email+", company = "+company+", last_name = "+last_name+", created_at = "+created_at+", account = "+account+", gender = "+gender+", comments = "+comments+", customer_id = "+customer_id+", address_one = "+address_one+", address_two = "+address_two+"]";
    }
}
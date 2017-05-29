package com.vpage.vpos.pojos.storeConfig;


public class UpdateStoreConfigRequest {

    private String company_phonenumber;

    private String fax;

    private String company_address;

    private String email;

    private String website;

    private String company_logo;

    private String company_name;

    private String return_policy;

    public String getCompany_phonenumber() {
        return company_phonenumber;
    }

    public void setCompany_phonenumber(String company_phonenumber) {
        this.company_phonenumber = company_phonenumber;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getCompany_address() {
        return company_address;
    }

    public void setCompany_address(String company_address) {
        this.company_address = company_address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getCompany_logo() {
        return company_logo;
    }

    public void setCompany_logo(String company_logo) {
        this.company_logo = company_logo;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getReturn_policy() {
        return return_policy;
    }

    public void setReturn_policy(String return_policy) {
        this.return_policy = return_policy;
    }

    @Override
    public String toString() {
        return "UpdateStoreConfigRequest{" +
                "company_phonenumber='" + company_phonenumber + '\'' +
                ", fax='" + fax + '\'' +
                ", company_address='" + company_address + '\'' +
                ", email='" + email + '\'' +
                ", website='" + website + '\'' +
                ", company_logo='" + company_logo + '\'' +
                ", company_name='" + company_name + '\'' +
                ", return_policy='" + return_policy + '\'' +
                '}';
    }
}

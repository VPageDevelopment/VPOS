package com.vpage.vpos.pojos.customer;

import java.util.Arrays;

public class CustomersResponse {

    private String status;

    private String status_code;

    private Customers[] customers;

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

    public Customers[] getCustomers ()
    {
        return customers;
    }

    public void setCustomers (Customers[] customers)
    {
        this.customers = customers;
    }

    @Override
    public String toString() {
        return "CustomersResponse{" +
                "status='" + status + '\'' +
                ", status_code='" + status_code + '\'' +
                ", customers=" + Arrays.toString(customers) +
                '}';
    }
}

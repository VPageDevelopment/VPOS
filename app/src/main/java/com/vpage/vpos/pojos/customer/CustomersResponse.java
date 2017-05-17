package com.vpage.vpos.pojos.customer;

import java.util.Arrays;

public class CustomersResponse {

    private Customers[] customers;

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
                "customers=" + Arrays.toString(customers) +
                '}';
    }
}

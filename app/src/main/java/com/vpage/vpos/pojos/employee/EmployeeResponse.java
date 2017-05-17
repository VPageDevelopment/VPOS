package com.vpage.vpos.pojos.employee;

import java.util.Arrays;

public class EmployeeResponse {

    private Employees[] employees;

    public Employees[] getEmployees() {
        return employees;
    }

    public void setEmployees(Employees[] employees) {
        this.employees = employees;
    }

    @Override
    public String toString() {
        return "EmployeeResponse{" +
                "employees=" + Arrays.toString(employees) +
                '}';
    }
}

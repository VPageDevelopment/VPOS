package com.vpage.vpos.pojos.employee;

import java.util.Arrays;

public class EmployeeResponse {

    private String status;

    private String status_code;

    private Employees[] Employees;

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

    public Employees[] getEmployees() {
        return Employees;
    }

    public void setEmployees(Employees[] employees) {
        this.Employees = employees;
    }

    @Override
    public String toString() {
        return "EmployeeResponse{" +
                "status='" + status + '\'' +
                ", status_code='" + status_code + '\'' +
                ", employees=" + Arrays.toString(Employees) +
                '}';
    }
}

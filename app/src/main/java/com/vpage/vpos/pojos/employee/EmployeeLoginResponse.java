package com.vpage.vpos.pojos.employee;


import java.util.Arrays;

public class EmployeeLoginResponse {

    private String status;

    private String status_code;

    private EmployeesLogins[] employeesLogins;

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

    public EmployeesLogins[] getEmployeesLogins() {
        return employeesLogins;
    }

    public void setEmployeesLogins(EmployeesLogins[] employeesLogins) {
        this.employeesLogins = employeesLogins;
    }

    @Override
    public String toString() {
        return "EmployeeLoginResponse{" +
                "status='" + status + '\'' +
                ", status_code='" + status_code + '\'' +
                ", employeesLogins=" + Arrays.toString(employeesLogins) +
                '}';
    }
}

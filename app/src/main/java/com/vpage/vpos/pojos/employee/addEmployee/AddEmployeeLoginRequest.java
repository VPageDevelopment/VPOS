package com.vpage.vpos.pojos.employee.addEmployee;


public class AddEmployeeLoginRequest {

    private String employee_fk;

    private String user_name;

    private String password;

    public String getEmployee_fk() {
        return employee_fk;
    }

    public void setEmployee_fk(String employee_fk) {
        this.employee_fk = employee_fk;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "AddEmployeeLoginRequest{" +
                "employee_fk='" + employee_fk + '\'' +
                ", user_name='" + user_name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

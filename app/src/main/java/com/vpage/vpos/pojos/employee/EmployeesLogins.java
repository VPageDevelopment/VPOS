package com.vpage.vpos.pojos.employee;

public class EmployeesLogins {

    private String employee_login_id;

    private String employee_fk;

    private String user_name;

    private String password;

    private String created_at;

    private String updated_at;

    public String getEmployee_login_id() {
        return employee_login_id;
    }

    public void setEmployee_login_id(String employee_login_id) {
        this.employee_login_id = employee_login_id;
    }

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

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    @Override
    public String toString() {
        return "EmployeesLogins{" +
                "employee_login_id='" + employee_login_id + '\'' +
                ", employee_fk='" + employee_fk + '\'' +
                ", user_name='" + user_name + '\'' +
                ", password='" + password + '\'' +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                '}';
    }
}

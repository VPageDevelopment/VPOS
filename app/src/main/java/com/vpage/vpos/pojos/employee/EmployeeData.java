package com.vpage.vpos.pojos.employee;

/**
 * Created by admin on 6/14/2017.
 */

public class EmployeeData {

    String name;
    public boolean isChecked;

    public EmployeeData(String name, boolean isChecked) {
        this.name = name;
        this.isChecked = isChecked;
    }

    @Override
    public String toString() {
        return "EmployeeData{" +
                "name='" + name + '\'' +
                ", isChecked=" + isChecked +
                '}';
    }
}

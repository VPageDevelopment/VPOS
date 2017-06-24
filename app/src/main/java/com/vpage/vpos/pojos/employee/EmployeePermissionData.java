package com.vpage.vpos.pojos.employee;

/**
 * Created by admin on 6/14/2017.
 */

public class EmployeePermissionData {

    String name;
    public boolean isChecked;

    public EmployeePermissionData(String name, boolean isChecked) {
        this.name = name;
        this.isChecked = isChecked;
    }

    @Override
    public String toString() {
        return "EmployeePermissionData{" +
                "name='" + name + '\'' +
                ", isChecked=" + isChecked +
                '}';
    }
}

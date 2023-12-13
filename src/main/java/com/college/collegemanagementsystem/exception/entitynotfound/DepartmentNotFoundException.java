package com.college.collegemanagementsystem.exception.entitynotfound;

public class DepartmentNotFoundException extends RuntimeException {
    public DepartmentNotFoundException(String departmentCode) {
        super("Department " + departmentCode + " doesn't exist");
    }
}

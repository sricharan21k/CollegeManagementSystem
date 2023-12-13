package com.college.collegemanagementsystem.exception.entityalreadyexists;

public class DepartmentAlreadyExistsException extends RuntimeException {
    public DepartmentAlreadyExistsException(String departmentCode) {
        super("Department " + departmentCode + " already exists");
    }
}

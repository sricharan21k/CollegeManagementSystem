package com.college.collegemanagementsystem.exception.entityalreadyexists;

public class StudentAlreadyExistsException extends RuntimeException {
    public StudentAlreadyExistsException(String rollNumber) {
        super("Student with roll number " + rollNumber + " already exists");
    }
}

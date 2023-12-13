package com.college.collegemanagementsystem.exception.entitynotfound;

public class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException(String rollNumber) {
        super("Student with roll number: " + rollNumber + " not found");
    }
}

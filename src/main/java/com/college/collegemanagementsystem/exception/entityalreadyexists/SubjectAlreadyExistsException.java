package com.college.collegemanagementsystem.exception.entityalreadyexists;

public class SubjectAlreadyExistsException extends RuntimeException {
    public SubjectAlreadyExistsException(String subjectCode) {
        super("Subject " + subjectCode + " already exists");
    }
}

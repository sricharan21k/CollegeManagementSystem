package com.college.collegemanagementsystem.exception.entitynotfound;

public class ProfessorNotFoundException extends RuntimeException {
    public ProfessorNotFoundException(String professorId) {
        super("Professor with id: " + professorId + " not found");
    }
}

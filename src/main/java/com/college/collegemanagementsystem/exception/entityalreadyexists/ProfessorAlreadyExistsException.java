package com.college.collegemanagementsystem.exception.entityalreadyexists;

public class ProfessorAlreadyExistsException extends RuntimeException {
    public ProfessorAlreadyExistsException(String professorId) {
        super("Professor with " + professorId + " already exists");
    }
}

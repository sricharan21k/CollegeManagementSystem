package com.college.collegemanagementsystem.exception.entitynotfound;

import java.util.List;

public class SubjectNotFoundException extends RuntimeException {
    public SubjectNotFoundException(String subjectCode) {
        super("Subject " + subjectCode + " doesn't exist");
    }

    public SubjectNotFoundException(List<String> subjects) {

        super("Subjects " + subjects + " not found");
    }
}

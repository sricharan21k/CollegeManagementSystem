package com.college.collegemanagementsystem.dto.request.update;

import lombok.Data;

import java.util.List;

@Data
public class UpdateProfessorRequest {
    private String professorId;
    private String firstname;
    private String lastname;
    private String departmentCode;
    private List<String> subjects;
}

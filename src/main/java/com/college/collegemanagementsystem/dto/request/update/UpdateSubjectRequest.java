package com.college.collegemanagementsystem.dto.request.update;

import lombok.Data;

@Data
public class UpdateSubjectRequest {
    private String code;
    private String name;
    private int credits;
    private String professorId;
}

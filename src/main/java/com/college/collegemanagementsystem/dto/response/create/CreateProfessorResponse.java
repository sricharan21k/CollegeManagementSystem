package com.college.collegemanagementsystem.dto.response.create;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateProfessorResponse {
    private String professorId;
    private String name;
    private String departmentCode;
}

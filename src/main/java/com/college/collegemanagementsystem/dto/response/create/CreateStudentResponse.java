package com.college.collegemanagementsystem.dto.response.create;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateStudentResponse {
    private String rollNumber;
    private String name;
    private String departmentCode;
}

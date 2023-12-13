package com.college.collegemanagementsystem.dto.response.create;

import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class CreateSubjectResponse {
    private String name;
    private String code;
    private int credits;
    private String departmentCode;
}

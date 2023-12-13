package com.college.collegemanagementsystem.dto.response.fetch;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FetchSubjectResponse {
    private String name;
    private String code;
    private int credits;
    private String departmentCode;
    private String professorName;
}

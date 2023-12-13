package com.college.collegemanagementsystem.dto.response.fetch;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FetchPartialStudentResponse {
    private String rollNumber;
    private String name;
    private String departmentCode;

}

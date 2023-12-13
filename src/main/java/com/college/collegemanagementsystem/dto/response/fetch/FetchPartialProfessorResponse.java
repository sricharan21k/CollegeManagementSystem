package com.college.collegemanagementsystem.dto.response.fetch;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FetchPartialProfessorResponse {
    private String professorId;
    private String name;
    private String departmentCode;
}

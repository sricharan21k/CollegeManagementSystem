package com.college.collegemanagementsystem.dto.response.create;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateDepartmentResponse {
    private String name;
    private String code;
}

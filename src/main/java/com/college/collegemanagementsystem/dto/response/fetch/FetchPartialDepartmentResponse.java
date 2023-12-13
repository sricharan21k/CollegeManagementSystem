package com.college.collegemanagementsystem.dto.response.fetch;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FetchPartialDepartmentResponse {
    private String name;
    private String code;
    private String hodName;
}

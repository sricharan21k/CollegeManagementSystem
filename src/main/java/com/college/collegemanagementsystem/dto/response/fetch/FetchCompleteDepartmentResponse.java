package com.college.collegemanagementsystem.dto.response.fetch;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FetchCompleteDepartmentResponse {

    private String name;
    private String code;
    private String hodName;
    private List<String> subjects;
    private int professorCount;
    private int studentCount;
}

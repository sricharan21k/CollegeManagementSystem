package com.college.collegemanagementsystem.dto.response.fetch;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FetchCompleteStudentResponse {
    private String rollNumber;
    private String name;
    private String departmentCode;
    private List<String> subjectsPassed;
    private int creditsEarned;
}

package com.college.collegemanagementsystem.dto.request.update;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
public class UpdateStudentRequest {
    private String firstname;
    private String lastname;
    private List<String> subjects;
}

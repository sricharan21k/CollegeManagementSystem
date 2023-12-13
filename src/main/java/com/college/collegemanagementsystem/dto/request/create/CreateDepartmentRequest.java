package com.college.collegemanagementsystem.dto.request.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CreateDepartmentRequest {

    @NotBlank(message = "Name cannot be empty")
    @Pattern(regexp = "^[A-Za-z -]+$", message = "Numbers and special characters other than hyphen are not allowed")
    private String name;
}

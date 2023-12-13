package com.college.collegemanagementsystem.dto.request.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateProfessorRequest {

    @NotBlank(message = "Firstname cannot be empty")
    @Pattern(regexp = "^[A-Za-z -]+$", message = "Numbers and special characters other than hyphen are not allowed")
    private String firstname;

    @NotBlank(message = "Lastname cannot be empty")
    @Pattern(regexp = "^[A-Za-z -]+$", message = "Numbers and special characters other than hyphen are not allowed")
    private String lastname;

    @NotBlank(message = "Department code cannot be empty")
    @Pattern(regexp = "^[A-Z]+$", message = "Only Capital letters are allowed")
    @Size(min = 2, max = 5, message = "Length should be 2 to 5 characters")
    private String departmentCode;
}

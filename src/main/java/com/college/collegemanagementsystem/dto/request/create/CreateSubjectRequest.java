package com.college.collegemanagementsystem.dto.request.create;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateSubjectRequest {

    @NotBlank(message = "Name cannot be empty")
    @Pattern(regexp = "^[A-Za-z -]+$", message = "Numbers and special characters other than hyphen are not allowed")
    private String name;

    @NotNull(message = "Credits cannot be null")
    @Min(value = 0)
    @Max(value = 3, message = "Value must be less than or equal to 3")
    private int credits;

    @NotBlank(message = "Department code cannot be empty")
    @Pattern(regexp = "^[A-Z]+$", message = "Only Capital letters are allowed")
    @Size(min = 2, max = 5, message = "Length should be 2 to 5 characters")
    private String departmentCode;


}

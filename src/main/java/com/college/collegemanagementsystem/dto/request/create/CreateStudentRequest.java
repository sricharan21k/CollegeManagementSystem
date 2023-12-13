package com.college.collegemanagementsystem.dto.request.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateStudentRequest {

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

   /*
    @NotBlank(message = "Roll number cannot be empty")
    @Size(min = 5, max = 10, message = "Id length should be 10 characters")
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "Special characters and spaces are not allowed")
    private String rollNumber;
    */
}

package com.college.collegemanagementsystem.controller;

import com.college.collegemanagementsystem.dto.request.create.CreateStudentRequest;
import com.college.collegemanagementsystem.model.Department;
import com.college.collegemanagementsystem.model.Student;
import com.college.collegemanagementsystem.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private StudentService studentService;

    @Test
    public void testCreateStudent() throws Exception {
        CreateStudentRequest request = new CreateStudentRequest();
        request.setDepartmentCode("CS");
        request.setFirstname("Shree");
        request.setLastname("Charan");

        Student student = Student.builder().id(101L).rollNumber("ST@123").firstname(request.getFirstname()).lastname(request.getLastname())
                .department(Department.builder().code("CS").build()).build();

        when(studentService.createStudent(request)).thenReturn(student);

        mockMvc.perform(post("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.rollNumber").value("ST@123"))
                .andExpect(jsonPath("$.name").value("Shree Charan"))
                .andExpect(jsonPath("$.departmentCode").value("CS"))
                .andExpect(header().string("Location", "/students/101"))
                .andDo(MockMvcResultHandlers.print());

    }

}
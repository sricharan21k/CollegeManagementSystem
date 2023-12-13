package com.college.collegemanagementsystem.controller;

import com.college.collegemanagementsystem.dto.request.create.CreateDepartmentRequest;
import com.college.collegemanagementsystem.dto.request.update.UpdateDepartmentRequest;
import com.college.collegemanagementsystem.model.Department;
import com.college.collegemanagementsystem.model.Professor;
import com.college.collegemanagementsystem.service.DepartmentService;
import com.college.collegemanagementsystem.service.ProfessorService;
import com.college.collegemanagementsystem.service.StudentService;
import com.college.collegemanagementsystem.service.SubjectService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(DepartmentController.class)
public class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private DepartmentService departmentService;
    @MockBean
    private StudentService studentService;
    @MockBean
    private SubjectService subjectService;
    @MockBean
    private ProfessorService professorService;

    @Test
    public void testGetDepartments() throws Exception {
        List<Department> departmentList = new ArrayList<>();
        departmentList.add(Department.builder().name("Computer Science").code("CS")
                .hod(Professor.builder().firstname("John").lastname("Carter").build()).build());
        departmentList.add(Department.builder().name("Information Technology").code("IT").hod(null).build());

        when(departmentService.getDepartments()).thenReturn(departmentList);

        mockMvc.perform(get("/departments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Computer Science"))
                .andExpect(jsonPath("$[1].name").value("Information Technology"));
    }

    @Test
    public void testGetDepartment() throws Exception {
        when(departmentService.getDepartment("CS"))
                .thenReturn(Department.builder().name("Computer Science").code("CS")
                        .hod(Professor.builder().firstname("Jason").lastname("Dan").build())
                        .subjects(Collections.emptyList())
                        .students(Collections.emptyList())
                        .professors(Collections.emptyList())
                        .build());

        mockMvc.perform(get("/departments/CS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Computer Science"))
                .andExpect(jsonPath("$.hodName").value("Jason Dan"))
                .andExpect(jsonPath("$.subjects").value(new ArrayList<>()))
                .andExpect(jsonPath("$.professorCount").value(0))
                .andExpect(jsonPath("$.studentCount").value(0));
    }

    @Test
    public void testCreateDepartment() throws Exception {
        CreateDepartmentRequest request = new CreateDepartmentRequest();
        request.setName("Information Technology");

        when(departmentService.createDepartment(request))
                .thenReturn(Department.builder().id(1L).name("Information Technology").code("IT").build());

        mockMvc.perform(post("/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Information Technology\"}")) //json representation of the request
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Information Technology"))
                .andExpect(jsonPath("$.code").value("IT"))
                .andExpect(header().string("Location", "/departments/1"));
    }

    @Test
    public void testUpdateDepartment() throws Exception {
        UpdateDepartmentRequest request = new UpdateDepartmentRequest();
        request.setHodId("ABCD");

        Department updatedDepartment = Department.builder().name("Computer Science").code("CS")
                .hod(Professor.builder().firstname("Alex").lastname("Jones").professorId("ABCD").build())
                .students(Collections.emptyList())
                .students(Collections.emptyList())
                .professors(Collections.emptyList())
                .build();

        when(departmentService.updateDepartment("CS", request))
        .thenReturn(updatedDepartment);

        mockMvc.perform(patch("/departments/CS")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"hodId\": \"ABCD\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Computer Science"))
                .andExpect(jsonPath("$.code").value("CS"))
                .andExpect(jsonPath("$.hodName").value("Alex Jones"))
                .andExpect(jsonPath("$.subjects").value(new ArrayList<>()))
                .andExpect(jsonPath("$.professorCount").value(0))
                .andExpect(jsonPath("$.studentCount").value(0));

    }
}

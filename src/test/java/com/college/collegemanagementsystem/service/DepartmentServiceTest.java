package com.college.collegemanagementsystem.service;

import com.college.collegemanagementsystem.common.UtilityFunctions;
import com.college.collegemanagementsystem.dto.request.create.CreateDepartmentRequest;
import com.college.collegemanagementsystem.dto.request.update.UpdateDepartmentRequest;
import com.college.collegemanagementsystem.exception.entityalreadyexists.DepartmentAlreadyExistsException;
import com.college.collegemanagementsystem.exception.entitynotfound.DepartmentNotFoundException;
import com.college.collegemanagementsystem.model.Department;
import com.college.collegemanagementsystem.model.Professor;
import com.college.collegemanagementsystem.repository.DepartmentRepository;
import com.college.collegemanagementsystem.service.common.ModelProvider;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private UtilityFunctions utilityFunctions;
    @Mock
    private ModelProvider modelProvider;

    @InjectMocks
    private DepartmentService departmentService;


    @Test
    public void testCreateDepartment() {
        CreateDepartmentRequest request = new CreateDepartmentRequest();
        request.setName("Computer Science");

        when(utilityFunctions.generateAcronym("Computer Science")).thenReturn("CS");
        when(departmentRepository.findByCode("CS")).thenReturn(Optional.empty());
        when(departmentRepository.save(any())).thenAnswer(invocationOnMock -> {
            Department department = invocationOnMock.getArgument(0);
            department.setId(1001L);
            return department;
        });

        Department createdDepartment = departmentService.createDepartment(request);

        verify(utilityFunctions, times(1)).generateAcronym("Computer Science");
        verify(departmentRepository, times(1)).findByCode("CS");
        verify(departmentRepository, times(1)).save(any(Department.class));

        assertThat(createdDepartment.getName()).isEqualTo("Computer Science");
        assertThat(createdDepartment.getCode()).isEqualTo("CS");
        assertThat(createdDepartment.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateDepartment_DepartmentAlreadyExists(){
        CreateDepartmentRequest request = new CreateDepartmentRequest();
        request.setName("Information Technology");

        when(utilityFunctions.generateAcronym("Information Technology")).thenReturn("IT");
        when(departmentRepository.findByCode("IT")).thenReturn(Optional.of(new Department()));

        assertThatThrownBy(() -> departmentService.createDepartment(request))
                .isInstanceOf(DepartmentAlreadyExistsException.class);
    }

    @Test
    public void testGetAllDepartments(){
        List<Department> departmentList = new ArrayList<>();
        departmentList.add(Department.builder().name("Computer Science").code("CS").build());
        departmentList.add(Department.builder().name("Information Technology").code("IT").build());
        when(departmentRepository.findAll()).thenReturn(departmentList);

        List<Department> departments = departmentService.getDepartments();

        assertThat(departments).isNotNull();
        assertThat(departments.size()).isEqualTo(2);
        assertThat(departments.stream().map(Department::getCode).toList()).contains("CS", "IT");
    }

    @Test
    public void testGetDepartment(){
        when(departmentRepository.findByCode("EE"))
                .thenReturn(Optional.ofNullable(
                        Department.builder()
                                .name("Electrical and Electronics")
                                .code("EE").build()
                ));

        assertThat(departmentService.getDepartment("EE")).isNotNull();
        assertThat(departmentService.getDepartment("EE").getName())
                .isEqualTo("Electrical and Electronics");
    }

    @Test
    public void testGetDepartment_NotFound(){
        when(departmentRepository.findByCode("EE")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> departmentService.getDepartment("EE"))
                .isInstanceOf(DepartmentNotFoundException.class);
    }

    @Test
    public void testUpdateDepartment(){
        UpdateDepartmentRequest request = new UpdateDepartmentRequest();
        request.setName("Electronics and Communication");
        request.setHodId("1001");

        Department existingDepartment = Department.builder()
                .name("Electrical and Electronics").code("EE").hod(null).build();

        Professor professor = Professor.builder()
                .id(1001L).firstname("Alex").build();

        when(departmentRepository.findByCode("EE")).thenReturn(Optional.of(existingDepartment));
        when(modelProvider.getProfessor("1001")).thenReturn(professor);
        when(departmentRepository.save(any())).thenReturn(existingDepartment);

        Department updatedDepartment = departmentService.updateDepartment("EE", request);

        assertThat(updatedDepartment).isNotNull();
        assertThat(updatedDepartment.getName()).isEqualTo("Electronics and Communication");
        assertThat(updatedDepartment.getHod().getId()).isEqualTo(1001L);
        assertThat(updatedDepartment.getHod().getFirstname()).isNotEqualTo("Alexa");

        verify(modelProvider, times(1)).getProfessor("1001");
        verify(departmentRepository, times(1))
                .save(Objects.requireNonNull(existingDepartment));

    }

}
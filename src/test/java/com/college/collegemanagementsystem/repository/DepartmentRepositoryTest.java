package com.college.collegemanagementsystem.repository;

import com.college.collegemanagementsystem.model.Department;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class DepartmentRepositoryTest {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Test
    public void testDepartmentFound(){

        Department department = Department.builder().name("Computer Science").code("CS").build();
        departmentRepository.save(department);

        Optional<Department> savedDepartment = departmentRepository.findByCode("CS");

        assertThat(savedDepartment).isPresent();
        assertThat(savedDepartment.get().getName()).isEqualTo("Computer Science");

    }

    @Test
    public void testDepartmentNotFound(){

        Optional<Department> savedDepartment = departmentRepository.findByCode("CS");

        assertThat(savedDepartment).isNotPresent();


    }
}

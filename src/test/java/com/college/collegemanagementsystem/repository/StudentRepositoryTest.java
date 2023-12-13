package com.college.collegemanagementsystem.repository;

import com.college.collegemanagementsystem.model.Department;
import com.college.collegemanagementsystem.model.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class StudentRepositoryTest {

    @Autowired private DepartmentRepository departmentRepository;
    @Autowired private StudentRepository studentRepository;

    @Test
    public void testFindStudent_ByRollNumber(){
        Student newStudent = Student.builder().rollNumber("ST@123").firstname("Shree").lastname("Charan").build();
        studentRepository.save(newStudent);

        Optional<Student> savedStudent = studentRepository.findByRollNumber("ST@123");

        assertThat(savedStudent).isPresent();
        assertThat(savedStudent.get().getFirstname()).isEqualTo("Shree");
    }

    @Test
    public void testStudentNotFound_ByRollNumber(){
        Optional<Student> student = studentRepository.findByRollNumber("123456");
        assertThat(student).isNotPresent();
    }

    @Test
    public void shouldFindAllStudents_ByDepartment(){

        Department department = Department.builder().code("CS").build();
        departmentRepository.save(department);

        List<Student> newStudents = List.of(Student.builder().firstname("Shree").department(department).build(),
                Student.builder().firstname("Alan").department(department).build());

        studentRepository.saveAll(newStudents);

        List<Student> savedStudents = studentRepository
                .findAllByDepartmentId(departmentRepository.findByCode("CS").get().getId());


        assertThat(savedStudents).isNotEmpty().hasSize(2);
        assertThat(studentRepository.findByRollNumber("123456")).isNotPresent();
    }

}
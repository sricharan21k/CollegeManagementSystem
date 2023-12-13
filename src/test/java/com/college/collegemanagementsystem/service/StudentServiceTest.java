package com.college.collegemanagementsystem.service;

import com.college.collegemanagementsystem.common.CustomDataType;
import com.college.collegemanagementsystem.common.UtilityFunctions;
import com.college.collegemanagementsystem.dto.request.create.CreateStudentRequest;
import com.college.collegemanagementsystem.dto.request.update.UpdateStudentRequest;
import com.college.collegemanagementsystem.model.Department;
import com.college.collegemanagementsystem.model.Student;
import com.college.collegemanagementsystem.model.Subject;
import com.college.collegemanagementsystem.repository.StudentRepository;
import com.college.collegemanagementsystem.service.common.ModelProvider;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@SpringBootTest
class StudentServiceTest {
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private ModelProvider modelProvider;
    @Mock
    private UtilityFunctions utilityFunctions;
    @InjectMocks
    private StudentService studentService;


    @Test
    public void testCreateStudent(){
        CreateStudentRequest request = new CreateStudentRequest();
        request.setFirstname("Shree");
        request.setLastname("Charan");
        request.setDepartmentCode("CS");

        Department department = Department.builder().name("Computer Science").code("CS").students(new ArrayList<>()).build();

        when(modelProvider.getDepartment("CS")).thenReturn(department);
        when(utilityFunctions.generateStudentRollNumber(any(), any(), any(), anyInt())).thenReturn("ST@SCCS001");
        when(studentRepository.save(any(Student.class))).thenAnswer(invocationOnMock -> {
            Student student = invocationOnMock.getArgument(0);
            student.setId(100L);
            return student;
        });

        Student createdStudent = studentService.createStudent(request);

        verify(modelProvider, times(1)).getDepartment("CS");
        verify(utilityFunctions, times(1)).generateStudentRollNumber(any(), any(), any(), anyInt());
        verify(studentRepository, times(1)).save(any(Student.class));

        assertThat(createdStudent).isNotNull();
        assertThat(createdStudent.getId()).isEqualTo(100);
        assertThat(createdStudent.getFirstname()).isEqualTo("Shree");
        assertThat(createdStudent.getRollNumber()).isEqualTo("ST@SCCS001");
        assertThat(createdStudent.getDepartment().getCode()).isEqualTo("CS");
    }

    @Test
    public void testUpdateStudent(){
        UpdateStudentRequest request = new UpdateStudentRequest();
        request.setFirstname("James");
        request.setSubjects(List.of("AAA", "BBB"));
        Subject subject1 = Subject.builder().code("AAA").credits(3).build();
        Subject subject2 = Subject.builder().code("CCC").credits(3).build();

        Student existingStudent = Student.builder().rollNumber("ST@123").firstname("Alex").lastname("Jones")
                .subjectsPassed(List.of(subject1, subject2)).build();

        when(studentRepository.findByRollNumber(any())).thenReturn(Optional.ofNullable(existingStudent));
        when(modelProvider.getSubject("AAA")).thenReturn(subject1);
        when(modelProvider.getSubject("BBB")).thenReturn(null);
        when(studentRepository.save(any(Student.class))).thenReturn(existingStudent);

        CustomDataType result = studentService.updateStudent("ST@123", request);
        List<String> subjectsNotFound = result.getStringList();
        Student updatedStudent = (Student) result.getObject();


        verify(studentRepository, times(1)).findByRollNumber(any());
        verify(modelProvider, times(1)).getSubject("AAA");
        verify(modelProvider, times(1)).getSubject("BBB");
        verify(studentRepository, times(1)).save(any(Student.class));


        assertThat(result).isNotNull();
        assertThat(subjectsNotFound).hasSize(1);
        assertThat(subjectsNotFound).contains("BBB");
        assertThat(result.getObject()).isNotNull();
        assertThat(updatedStudent.getFirstname()).isEqualTo("James");
        assertThat(updatedStudent.getCreditsEarned()).isEqualTo(6);
        assertThat(updatedStudent.getSubjectsPassed()).hasSize(2);
        assertThat(updatedStudent.getSubjectsPassed()).contains(subject1, subject2);

    }

}
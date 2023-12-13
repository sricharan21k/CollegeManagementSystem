package com.college.collegemanagementsystem.controller;

import com.college.collegemanagementsystem.dto.request.create.CreateDepartmentRequest;
import com.college.collegemanagementsystem.dto.request.update.UpdateDepartmentRequest;
import com.college.collegemanagementsystem.dto.response.create.CreateDepartmentResponse;
import com.college.collegemanagementsystem.dto.response.fetch.*;
import com.college.collegemanagementsystem.model.Department;
import com.college.collegemanagementsystem.model.Professor;
import com.college.collegemanagementsystem.model.Student;
import com.college.collegemanagementsystem.model.Subject;
import com.college.collegemanagementsystem.service.DepartmentService;
import com.college.collegemanagementsystem.service.ProfessorService;
import com.college.collegemanagementsystem.service.StudentService;
import com.college.collegemanagementsystem.service.SubjectService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/departments")
public class DepartmentController {
    private final DepartmentService departmentService;
    private final StudentService studentService;
    private final SubjectService subjectService;
    private final ProfessorService professorService;

    public DepartmentController(DepartmentService departmentService,
                                StudentService studentService,
                                SubjectService subjectService,
                                ProfessorService professorService) {
        this.departmentService = departmentService;
        this.studentService = studentService;
        this.subjectService = subjectService;
        this.professorService = professorService;
    }

    @GetMapping
    public ResponseEntity<List<FetchPartialDepartmentResponse>> getDepartments() {
        var departments = departmentService.getDepartments();

        List<FetchPartialDepartmentResponse> responses = new ArrayList<>();

        for (Department department : departments) {
            String hodName = department.getHod() == null ? "Not Specified"
                    : department.getHod().getFirstname()
                    + " " + department.getHod().getLastname();
            responses.add(
                    FetchPartialDepartmentResponse.builder()
                            .name(department.getName())
                            .code(department.getCode())
                            .hodName(hodName)
                            .build()
            );
        }

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{code}")
    public ResponseEntity<FetchCompleteDepartmentResponse> getDepartment(
            @PathVariable("code") String departmentCode) {

        var department = departmentService.getDepartment(departmentCode);

        var response = FetchCompleteDepartmentResponse.builder()
                .name(department.getName())
                .code(department.getCode())
                .hodName(department.getHod() == null ? "Not Specified"
                        : department.getHod().getFirstname()
                        + " " + department.getHod().getLastname())
                .subjects(department.getSubjects().stream()
                        .map(Subject::getCode).collect(Collectors.toList()))
                .professorCount(department.getProfessors().size())
                .studentCount(department.getStudents().size())
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<CreateDepartmentResponse> createDepartment(
            @Valid @RequestBody CreateDepartmentRequest request) {

        var newDepartment = departmentService.createDepartment(request);
        var response = CreateDepartmentResponse.builder()
                .name(newDepartment.getName())
                .code(newDepartment.getCode())
                .build();

        URI uri = URI.create("/departments/" + newDepartment.getId());

        return ResponseEntity.created(uri).body(response);
    }


    @PatchMapping("/{code}")
    public ResponseEntity<FetchCompleteDepartmentResponse> updateDepartment(
            @PathVariable("code") String departmentCode,
            @RequestBody UpdateDepartmentRequest request) {

        var department = departmentService.updateDepartment(departmentCode, request);
        var response = FetchCompleteDepartmentResponse.builder()
                .name(department.getName())
                .code(department.getCode())
                .hodName(department.getHod().getFirstname()
                        + " " + department.getHod().getLastname())
                .subjects(department.getSubjects() != null
                        ? department.getSubjects().stream().map(Subject::getCode).collect(Collectors.toList())
                        : Collections.emptyList())
                .professorCount(department.getProfessors().size())
                .studentCount(department.getStudents().size())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{code}/professors")
    public ResponseEntity<List<FetchPartialProfessorResponse>> getProfessors(
            @PathVariable("code") String departmentCode) {

        List<FetchPartialProfessorResponse> responses = new ArrayList<>();
        var professors = professorService.getProfessors(departmentCode);
        for (Professor professor : professors) {
            responses.add(
                    FetchPartialProfessorResponse.builder()
                            .professorId(professor.getProfessorId())
                            .name(professor.getFirstname() + " " + professor.getLastname())
                            .departmentCode(professor.getDepartment().getCode())
                            .build()
            );
        }

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{code}/subjects")
    public ResponseEntity<List<FetchSubjectResponse>> getSubjects(
            @PathVariable("code") String departmentCode) {

        List<FetchSubjectResponse> responses = new ArrayList<>();
        var subjects = subjectService.getSubjects(departmentCode);
        for (Subject subject : subjects) {
            String professorName = subject.getProfessor() != null
                    ? subject.getProfessor().getFirstname()
                    + " " + subject.getProfessor().getLastname()
                    : "Not specified";
            responses.add(
                    FetchSubjectResponse.builder()
                            .code(subject.getCode())
                            .name(subject.getName())
                            .credits(subject.getCredits())
                            .departmentCode(subject.getDepartment().getCode())
                            .professorName(professorName)
                            .build()
            );
        }

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{code}/students")
    public ResponseEntity<List<FetchPartialStudentResponse>> getStudents(
            @PathVariable("code") String departmentCode) {

        List<FetchPartialStudentResponse> responses = new ArrayList<>();
        var students = studentService.getStudents(departmentCode);
        for (Student student : students) {
            responses.add(
                    FetchPartialStudentResponse.builder()
                            .rollNumber(student.getRollNumber())
                            .name(student.getFirstname() + " " + student.getLastname())
                            .departmentCode(student.getDepartment().getCode())
                            .build()
            );
        }

        return ResponseEntity.ok(responses);
    }

}

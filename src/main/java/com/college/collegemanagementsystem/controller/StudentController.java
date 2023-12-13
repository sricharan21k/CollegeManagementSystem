package com.college.collegemanagementsystem.controller;

import com.college.collegemanagementsystem.dto.request.create.CreateStudentRequest;
import com.college.collegemanagementsystem.dto.request.update.UpdateStudentRequest;
import com.college.collegemanagementsystem.dto.response.create.CreateStudentResponse;
import com.college.collegemanagementsystem.dto.response.delete.DeleteResponse;
import com.college.collegemanagementsystem.dto.response.error.ErrorResponse;
import com.college.collegemanagementsystem.dto.response.fetch.FetchCompleteStudentResponse;
import com.college.collegemanagementsystem.dto.response.fetch.FetchPartialStudentResponse;
import com.college.collegemanagementsystem.exception.entitynotfound.SubjectNotFoundException;
import com.college.collegemanagementsystem.model.Student;
import com.college.collegemanagementsystem.model.Subject;
import com.college.collegemanagementsystem.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/students")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public ResponseEntity<List<FetchPartialStudentResponse>> getStudents() {
        var students = studentService.getAllStudents();
        List<FetchPartialStudentResponse> responses = new ArrayList<>();
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

    @GetMapping("/{roll}")
    public ResponseEntity<FetchCompleteStudentResponse> getStudent(
            @PathVariable("roll") String rollNumber) {

        var student = studentService.getStudent(rollNumber);
        var response = FetchCompleteStudentResponse.builder()
                .rollNumber(student.getRollNumber())
                .name(student.getFirstname() + " " + student.getLastname())
                .departmentCode(student.getDepartment().getCode())
                .subjectsPassed(student.getSubjectsPassed().stream()
                        .map(Subject::getCode).collect(Collectors.toList()))
                .creditsEarned(student.getCreditsEarned())
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<CreateStudentResponse> createStudent(
            @Valid @RequestBody CreateStudentRequest request) {
        var student = studentService.createStudent(request);
        var response = CreateStudentResponse.builder()
                .rollNumber(student.getRollNumber())
                .name(student.getFirstname() + " " + student.getLastname())
                .departmentCode(student.getDepartment().getCode())
                .build();

        URI uri = URI.create("/students/" + student.getId());
        return ResponseEntity.created(uri).body(response);
    }

    @PatchMapping("/{roll}")
    public ResponseEntity<?> updateStudent(
            @PathVariable("roll") String rollNumber,
            @RequestBody UpdateStudentRequest request) {

        try {
            var heterogeneousPayload = studentService.updateStudent(rollNumber, request);
            List<String> noSubjectsFound = heterogeneousPayload.getStringList();
            Student student = (Student) heterogeneousPayload.getObject();

            var response = FetchCompleteStudentResponse.builder()
                    .rollNumber(student.getRollNumber())
                    .name(student.getFirstname() + " " + student.getLastname())
                    .departmentCode(student.getDepartment().getCode())
                    .subjectsPassed(student.getSubjectsPassed().stream()
                            .map(Subject::getCode).collect(Collectors.toList()))
                    .creditsEarned(student.getCreditsEarned())
                    .build();

            if (noSubjectsFound.isEmpty()) {
                return ResponseEntity.ok().body(response);
            } else {
                throw new SubjectNotFoundException(noSubjectsFound);
            }

        } catch (SubjectNotFoundException ex) {

            HttpStatus status = HttpStatus.PARTIAL_CONTENT;
            var response = ErrorResponse.create(status, "Data is updated partially! " + ex.getMessage());

            return ResponseEntity.status(status).body(response);
        }
    }

    @DeleteMapping("/{roll}")
    public ResponseEntity<DeleteResponse> deleteStudent(
            @PathVariable("roll") String rollNumber) {

        HttpStatus status;
        String message;

        if (studentService.deleteStudent(rollNumber)) {
            status = HttpStatus.OK;
            message = "Delete request successfully processed";
        } else {
            status = HttpStatus.NOT_FOUND;
            message = "Delete request not processed";
        }

        var response = DeleteResponse.builder()
                .code(status.value())
                .status(status.getReasonPhrase())
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(status).body(response);
    }

}

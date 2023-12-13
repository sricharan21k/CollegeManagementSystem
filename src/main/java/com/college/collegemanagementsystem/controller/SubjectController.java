package com.college.collegemanagementsystem.controller;

import com.college.collegemanagementsystem.dto.request.create.CreateSubjectRequest;
import com.college.collegemanagementsystem.dto.request.update.UpdateSubjectRequest;
import com.college.collegemanagementsystem.dto.response.create.CreateSubjectResponse;
import com.college.collegemanagementsystem.dto.response.fetch.FetchSubjectResponse;
import com.college.collegemanagementsystem.model.Subject;
import com.college.collegemanagementsystem.service.SubjectService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/subjects")
public class SubjectController {
    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping
    public ResponseEntity<List<FetchSubjectResponse>> getSubjects() {
        var subjects = subjectService.getAllSubjects();
        List<FetchSubjectResponse> responses = new ArrayList<>();
        for (Subject subject : subjects) {
            String professorName = subject.getProfessor() == null
                    ? "Not specified"
                    : subject.getProfessor().getFirstname()
                    + subject.getProfessor().getLastname();
            responses.add(
                    FetchSubjectResponse.builder()
                            .name(subject.getName())
                            .code(subject.getCode())
                            .credits(subject.getCredits())
                            .departmentCode(subject.getDepartment().getCode())
                            .professorName(professorName)
                            .build()
            );
        }
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{code}")
    public ResponseEntity<FetchSubjectResponse> getSubject(@PathVariable("code") String subjectCode) {
        var subject = subjectService.getSubject(subjectCode);
        var response = FetchSubjectResponse.builder()
                .name(subject.getName())
                .code(subject.getCode())
                .credits(subject.getCredits())
                .departmentCode(subject.getDepartment().getCode())
                .professorName(subject.getProfessor() != null
                        ? subject.getProfessor().getFirstname()
                        + " " + subject.getProfessor().getLastname()
                        : "Not specified")
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<CreateSubjectResponse> createSubject(
            @Valid @RequestBody CreateSubjectRequest request) {
        var subject = subjectService.createSubject(request);
        var response = CreateSubjectResponse.builder()
                .name(subject.getName())
                .code(subject.getCode())
                .credits(subject.getCredits())
                .departmentCode(subject.getDepartment().getCode())
                .build();

        URI uri = URI.create("/subjects/" + subject.getId());
        return ResponseEntity.created(uri).body(response);
    }

    @PatchMapping("/{subjectCode}")
    public ResponseEntity<FetchSubjectResponse> updateSubject(
            @PathVariable String subjectCode,
            @RequestBody UpdateSubjectRequest request) {
        var subject = subjectService.updateSubject(subjectCode, request);
        var response = FetchSubjectResponse.builder()
                .code(subject.getCode())
                .name(subject.getName())
                .credits(subject.getCredits())
                .departmentCode(subject.getDepartment().getCode())
                .professorName(subject.getProfessor() != null
                        ? subject.getProfessor().getFirstname()
                        + " " + subject.getProfessor().getLastname()
                        : "Not specified")
                .build();

        return ResponseEntity.ok(response);
    }
}

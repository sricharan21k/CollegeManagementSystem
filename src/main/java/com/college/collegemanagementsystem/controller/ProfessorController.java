package com.college.collegemanagementsystem.controller;

import com.college.collegemanagementsystem.dto.request.create.CreateProfessorRequest;
import com.college.collegemanagementsystem.dto.request.update.UpdateProfessorRequest;
import com.college.collegemanagementsystem.dto.response.create.CreateProfessorResponse;
import com.college.collegemanagementsystem.dto.response.delete.DeleteResponse;
import com.college.collegemanagementsystem.dto.response.error.ErrorResponse;
import com.college.collegemanagementsystem.dto.response.fetch.FetchCompleteProfessorResponse;
import com.college.collegemanagementsystem.dto.response.fetch.FetchPartialProfessorResponse;
import com.college.collegemanagementsystem.exception.entitynotfound.SubjectNotFoundException;
import com.college.collegemanagementsystem.model.Professor;
import com.college.collegemanagementsystem.model.Subject;
import com.college.collegemanagementsystem.service.ProfessorService;
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
@RequestMapping("/professors")
public class ProfessorController {

    private final ProfessorService professorService;

    public ProfessorController(ProfessorService professorService) {
        this.professorService = professorService;
    }

    @GetMapping
    private ResponseEntity<List<FetchPartialProfessorResponse>> getProfessors() {
        List<FetchPartialProfessorResponse> responses = new ArrayList<>();
        var professors = professorService.getAllProfessors();
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

    @PostMapping
    public ResponseEntity<CreateProfessorResponse> createProfessor(
            @Valid @RequestBody CreateProfessorRequest request) {

        var professor = professorService.createProfessor(request);

        var response = CreateProfessorResponse.builder()
                .professorId(professor.getProfessorId())
                .name(professor.getFirstname() + " " + professor.getLastname())
                .departmentCode(professor.getDepartment().getCode())
                .build();

        URI uri = URI.create("/professors/");
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/{professorId}")
    public ResponseEntity<FetchCompleteProfessorResponse> getProfessor(@PathVariable String professorId) {
        var professor = professorService.getProfessor(professorId);
        var response = FetchCompleteProfessorResponse.builder()
                .professorId(professor.getProfessorId())
                .name(professor.getFirstname() + " " + professor.getLastname())
                .departmentCode(professor.getDepartment().getCode())
                .subjectsTaught(professor.getSubjectsTaught().stream()
                        .map(Subject::getCode).collect(Collectors.toList()))
                .build();

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{professorId}")
    public ResponseEntity<?> updateProfessor(
            @PathVariable String professorId,
            @RequestBody UpdateProfessorRequest request) {

        try {

            var data = professorService.updateProfessor(professorId, request);
            List<String> noSubjectsFound = data.getStringList();
            Professor professor = (Professor) data.getObject();

            var response = FetchCompleteProfessorResponse.builder()
                    .professorId(professor.getProfessorId())
                    .name(professor.getFirstname() + " " + professor.getLastname())
                    .departmentCode(professor.getDepartment().getCode())
                    .subjectsTaught(professor.getSubjectsTaught().stream()
                            .map(Subject::getCode).collect(Collectors.toList()))
                    .build();

            if (noSubjectsFound.isEmpty()) {
                return ResponseEntity.ok(response);
            } else {
                throw new SubjectNotFoundException(noSubjectsFound);
            }

        } catch (SubjectNotFoundException ex) {

            HttpStatus status = HttpStatus.PARTIAL_CONTENT;
            var response = ErrorResponse.create(status, "Data is updated partially! " + ex.getMessage());

            return ResponseEntity.status(status).body(response);
        }
    }

    @DeleteMapping("/{professorId}")
    public ResponseEntity<DeleteResponse> deleteProfessor(@PathVariable String professorId) {

        HttpStatus status;
        String message;

        if (professorService.deleteProfessor(professorId)) {
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

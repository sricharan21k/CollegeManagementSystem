package com.college.collegemanagementsystem.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String rollNumber;
    private String firstname;
    private String lastname;

    @ManyToMany
    @JoinTable(name = "student_subject",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id"))
    private List<Subject> subjectsPassed = new ArrayList<>();

    @ManyToOne
    @JsonIgnoreProperties("students")
    private Department department;

    @Transient
    private int creditsEarned;

    @JsonGetter("creditsEarned")
    public int getCreditsEarned() {
        return subjectsPassed == null ? 0 : subjectsPassed
                .stream()
                .mapToInt(Subject::getCredits)
                .sum();
    }

}

package com.college.collegemanagementsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String code;
    private Integer credits;

    @ManyToOne
    @JsonIgnoreProperties("subjects")
    private Department department;

    @ManyToOne
    @JsonIgnoreProperties("subjectsTaught")
    private Professor professor;
}

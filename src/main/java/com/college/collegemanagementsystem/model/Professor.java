package com.college.collegemanagementsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Professor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String professorId;
    private String firstname;
    private String lastname;

    @ManyToOne
    @JsonIgnoreProperties("professors")
    private Department department;

    @OneToMany(mappedBy = "professor")
    @JsonIgnore
    private List<Subject> subjectsTaught = new ArrayList<>();
}

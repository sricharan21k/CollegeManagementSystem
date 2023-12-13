package com.college.collegemanagementsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String code;

    @OneToOne
    @JoinColumn(unique = true)
    private Professor hod;

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Subject> subjects = new ArrayList<>();

    @OneToMany(mappedBy = "department")
    @JsonIgnore
    private List<Professor> professors = new ArrayList<>();

    @OneToMany(mappedBy = "department")
    @JsonIgnore
    private List<Student> students = new ArrayList<>();
}

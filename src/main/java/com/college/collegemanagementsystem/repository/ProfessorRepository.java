package com.college.collegemanagementsystem.repository;

import com.college.collegemanagementsystem.model.Professor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {
    List<Professor> findAllByDepartmentId(Long departmentId);

    Optional<Professor> findByProfessorId(String professorId);

}

package com.college.collegemanagementsystem.repository;

import com.college.collegemanagementsystem.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    List<Subject> findAllByDepartmentId(Long departmentId);

    Optional<Subject> findByCode(String subjectCode);

    Optional<Subject> findByCodeAndDepartmentId(String subjectCode, Long departmentId);
}

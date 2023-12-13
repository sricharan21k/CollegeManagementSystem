package com.college.collegemanagementsystem.repository;

import com.college.collegemanagementsystem.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findAllByDepartmentId(Long departmentId);

    Optional<Student> findByRollNumber(String rollNumber);
}

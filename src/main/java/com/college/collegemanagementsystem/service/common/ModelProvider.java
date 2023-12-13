package com.college.collegemanagementsystem.service.common;

import com.college.collegemanagementsystem.exception.entitynotfound.DepartmentNotFoundException;
import com.college.collegemanagementsystem.exception.entitynotfound.ProfessorNotFoundException;
import com.college.collegemanagementsystem.exception.entitynotfound.SubjectNotFoundException;
import com.college.collegemanagementsystem.model.Department;
import com.college.collegemanagementsystem.model.Professor;
import com.college.collegemanagementsystem.model.Subject;
import com.college.collegemanagementsystem.repository.DepartmentRepository;
import com.college.collegemanagementsystem.repository.ProfessorRepository;
import com.college.collegemanagementsystem.repository.SubjectRepository;
import org.springframework.stereotype.Service;

@Service
public class ModelProvider {
    private final DepartmentRepository departmentRepository;
    private final ProfessorRepository professorRepository;
    private final SubjectRepository subjectRepository;

    public ModelProvider(DepartmentRepository departmentRepository,
                         ProfessorRepository professorRepository,
                         SubjectRepository subjectRepository) {
        this.departmentRepository = departmentRepository;
        this.professorRepository = professorRepository;
        this.subjectRepository = subjectRepository;
    }

    public Department getDepartment(String departmentCode){
        return departmentRepository.findByCode(departmentCode).orElse(null);
    }

    public Professor getProfessor(String professorId){
        return professorRepository.findByProfessorId(professorId).orElse(null);
    }

    public Subject getSubject(String subjectCode){
        return subjectRepository.findByCode(subjectCode).orElse(null);
    }

    public Subject getSubject(String subjectCode, Long departmentId){
        return subjectRepository.findByCodeAndDepartmentId(subjectCode, departmentId).orElse(null);
    }
}

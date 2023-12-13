package com.college.collegemanagementsystem.service.common;

import com.college.collegemanagementsystem.model.Professor;
import com.college.collegemanagementsystem.model.Subject;
import com.college.collegemanagementsystem.repository.ProfessorRepository;
import com.college.collegemanagementsystem.repository.SubjectRepository;
import org.springframework.stereotype.Service;

@Service
public class ModelUpdater {
    private final SubjectRepository subjectRepository;
    private final ProfessorRepository professorRepository;

    public ModelUpdater(SubjectRepository subjectRepository, ProfessorRepository professorRepository) {
        this.subjectRepository = subjectRepository;
        this.professorRepository = professorRepository;
    }

    public void updateSubject(Subject subject){
        subjectRepository.save(subject);
    }

    public void updateProfessor(Professor professor){
        professorRepository.save(professor);
    }
}

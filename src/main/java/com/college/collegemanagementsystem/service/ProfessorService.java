package com.college.collegemanagementsystem.service;

import com.college.collegemanagementsystem.common.CustomDataType;
import com.college.collegemanagementsystem.common.UtilityFunctions;
import com.college.collegemanagementsystem.dto.request.create.CreateProfessorRequest;
import com.college.collegemanagementsystem.dto.request.update.UpdateProfessorRequest;
import com.college.collegemanagementsystem.exception.entitynotfound.DepartmentNotFoundException;
import com.college.collegemanagementsystem.exception.entitynotfound.ProfessorNotFoundException;
import com.college.collegemanagementsystem.model.Professor;
import com.college.collegemanagementsystem.model.Subject;
import com.college.collegemanagementsystem.repository.ProfessorRepository;
import com.college.collegemanagementsystem.service.common.ModelProvider;
import com.college.collegemanagementsystem.service.common.ModelUpdater;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProfessorService {
    private final ProfessorRepository professorRepository;
    private final ModelProvider modelProvider;
    private final ModelUpdater modelUpdater;
    private final UtilityFunctions utilityFunctions;

    public ProfessorService(ProfessorRepository professorRepository,
                            ModelProvider modelProvider,
                            ModelUpdater modelUpdater,
                            UtilityFunctions utilityFunctions) {
        this.professorRepository = professorRepository;
        this.modelProvider = modelProvider;
        this.modelUpdater = modelUpdater;
        this.utilityFunctions = utilityFunctions;
    }

    public List<Professor> getAllProfessors() {
        return professorRepository.findAll();
    }

    public List<Professor> getProfessors(String departmentCode) {
        var department = modelProvider.getDepartment(departmentCode);
        return professorRepository.findAllByDepartmentId(department.getId());
    }

    public Professor getProfessor(String professorId) {
        return professorRepository.findByProfessorId(professorId)
                .orElseThrow(() -> new ProfessorNotFoundException(professorId));
    }


    public Professor createProfessor(CreateProfessorRequest request) {

        var department = modelProvider.getDepartment(request.getDepartmentCode());
        if(department == null) throw new DepartmentNotFoundException(request.getDepartmentCode());

        var professor = Professor.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .professorId(
                        utilityFunctions.generateProfessorId(
                                request.getFirstname(),
                                request.getLastname(),
                                department.getCode(),
                                department.getProfessors().size()
                        )
                )
                .department(department)
                .build();

        return professorRepository.save(professor);
    }

    @Transactional
    public CustomDataType updateProfessor(String professorId, UpdateProfessorRequest request) {
        var professor = professorRepository.findByProfessorId(professorId)
                .orElseThrow(() -> new ProfessorNotFoundException(professorId));

        List<String> noSubjectsFound = new ArrayList<>();

        if (request.getProfessorId() != null) professor.setProfessorId(request.getProfessorId());

        if (request.getFirstname() != null) professor.setFirstname(request.getFirstname());

        if (request.getLastname() != null) professor.setLastname(request.getLastname());

        if (request.getDepartmentCode() != null) {
            professor.setDepartment(modelProvider.getDepartment(request.getDepartmentCode()));
        }

        if (!request.getSubjects().isEmpty()) {
            List<Subject> subjects = professor.getSubjectsTaught();
            for (String subjectCode : request.getSubjects()) {
                Subject subject = modelProvider.getSubject(subjectCode, professor.getDepartment().getId());
                if(subject == null ){
                    noSubjectsFound.add(subjectCode);
                }
                if (subject != null && !subjects.contains(subject)) {
                    subject.setProfessor(professor);
                    modelUpdater.updateSubject(subject);
                    subjects.add(subject);
                }
            }
            professor.setSubjectsTaught(subjects);
        }

        return CustomDataType.builder()
                .stringList(noSubjectsFound)
                .object(professorRepository.save(professor))
                .build();
    }

    @Transactional
    public boolean deleteProfessor(String professorId) {
        try {
            var professor = professorRepository.findByProfessorId(professorId)
                    .orElseThrow(() -> new ProfessorNotFoundException(professorId));

            for (Subject subject : professor.getSubjectsTaught()) {
                subject.setProfessor(null);
                modelUpdater.updateSubject(subject);
            }
            professorRepository.delete(professor);

            return true;
        }catch (ProfessorNotFoundException ex){
            return false;
        }
    }
}

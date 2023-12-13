package com.college.collegemanagementsystem.service;

import com.college.collegemanagementsystem.common.UtilityFunctions;
import com.college.collegemanagementsystem.dto.request.create.CreateSubjectRequest;
import com.college.collegemanagementsystem.dto.request.update.UpdateSubjectRequest;
import com.college.collegemanagementsystem.exception.entityalreadyexists.SubjectAlreadyExistsException;
import com.college.collegemanagementsystem.exception.entitynotfound.SubjectNotFoundException;
import com.college.collegemanagementsystem.model.Subject;
import com.college.collegemanagementsystem.repository.SubjectRepository;
import com.college.collegemanagementsystem.service.common.ModelProvider;
import com.college.collegemanagementsystem.service.common.ModelUpdater;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectService {
    private final SubjectRepository subjectRepository;
    private final ModelProvider modelProvider;
    private final ModelUpdater modelUpdater;
    private final UtilityFunctions utilityFunctions;

    public SubjectService(SubjectRepository subjectRepository,
                          ModelProvider modelProvider,
                          ModelUpdater modelUpdater,
                          UtilityFunctions utilityFunctions) {
        this.subjectRepository = subjectRepository;
        this.modelProvider = modelProvider;
        this.modelUpdater = modelUpdater;
        this.utilityFunctions = utilityFunctions;
    }

    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    public List<Subject> getSubjects(String departmentCode) {
        var department = modelProvider.getDepartment(departmentCode);
        return subjectRepository.findAllByDepartmentId(department.getId());
    }

    public Subject getSubject(String subjectCode) {
        return subjectRepository.findByCode(subjectCode)
                .orElseThrow(() -> new SubjectNotFoundException(subjectCode));
    }

    public Subject createSubject(CreateSubjectRequest request) {
        String code = utilityFunctions.generateAcronym(request.getName());
        boolean subjectExists = subjectRepository.findByCode(code).isPresent()
                && subjectRepository.findByCode(code).get()
                .getDepartment().getCode().equalsIgnoreCase(request.getDepartmentCode());

        if (subjectExists) throw new SubjectAlreadyExistsException(code);

        var newSubject = Subject.builder()
                .name(request.getName())
                .code(!request.getName().isEmpty() ? utilityFunctions.generateAcronym(request.getName()) : "")
                .credits(request.getCredits())
                .department(modelProvider.getDepartment(request.getDepartmentCode()))
                .build();

        return subjectRepository.save(newSubject);
    }

    @Transactional
    public Subject updateSubject(String subjectCode, UpdateSubjectRequest request) {

        var subject = subjectRepository.findByCode(subjectCode)
                .orElseThrow(() -> new SubjectNotFoundException(subjectCode));

        if (request.getCode() != null) subject.setCode(request.getCode());

        if (request.getName() != null) subject.setName(request.getName());

        if (request.getCredits() != 0) subject.setCredits(request.getCredits()); //TODO: check the credits properly

        if (request.getProfessorId() != null) {
            var professor = modelProvider.getProfessor(request.getProfessorId());
            subject.setProfessor(professor);
            professor.getSubjectsTaught().remove(subject);
            modelUpdater.updateProfessor(professor);
        }

        return subjectRepository.save(subject);
    }
}

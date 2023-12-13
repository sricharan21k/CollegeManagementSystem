package com.college.collegemanagementsystem.service;

import com.college.collegemanagementsystem.common.UtilityFunctions;
import com.college.collegemanagementsystem.dto.request.create.CreateDepartmentRequest;
import com.college.collegemanagementsystem.dto.request.update.UpdateDepartmentRequest;
import com.college.collegemanagementsystem.exception.entitynotfound.DepartmentNotFoundException;
import com.college.collegemanagementsystem.exception.entityalreadyexists.DepartmentAlreadyExistsException;
import com.college.collegemanagementsystem.exception.entitynotfound.ProfessorNotFoundException;
import com.college.collegemanagementsystem.model.Department;
import com.college.collegemanagementsystem.model.Professor;
import com.college.collegemanagementsystem.repository.DepartmentRepository;
import com.college.collegemanagementsystem.service.common.ModelProvider;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final ModelProvider modelProvider;
    private final UtilityFunctions utilityFunctions;

    public DepartmentService(DepartmentRepository departmentRepository,
                             ModelProvider modelProvider,
                             UtilityFunctions utilityFunctions) {
        this.departmentRepository = departmentRepository;
        this.modelProvider = modelProvider;
        this.utilityFunctions = utilityFunctions;
    }

    public List<Department> getDepartments() {
        return departmentRepository.findAll();
    }

    public Department createDepartment(CreateDepartmentRequest request) {

        String code = utilityFunctions.generateAcronym(request.getName());
        boolean departmentExists = departmentRepository.findByCode(code).isPresent();
        if (departmentExists) throw new DepartmentAlreadyExistsException(code);

        var department = Department.builder()
                .name(request.getName())
                .code(code)
                .build();

        return departmentRepository.save(department);
    }

    public Department getDepartment(String departmentCode) {
        return departmentRepository
                .findByCode(departmentCode)
                .orElseThrow(() -> new DepartmentNotFoundException(departmentCode));
    }

    @Transactional
    public Department updateDepartment(String departmentCode, UpdateDepartmentRequest request) {
        var department = departmentRepository
                .findByCode(departmentCode)
                .orElseThrow(() -> new DepartmentNotFoundException(departmentCode));

        if (request.getName() != null) {
            department.setName(request.getName());
        }

        if (request.getHodId() != null) {
            Professor professor = modelProvider.getProfessor(request.getHodId());
            if(professor == null) throw new ProfessorNotFoundException(request.getHodId());
            department.setHod(modelProvider.getProfessor(request.getHodId()));
        }

        return departmentRepository.save(department);
    }
}


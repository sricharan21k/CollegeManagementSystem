package com.college.collegemanagementsystem.service;

import com.college.collegemanagementsystem.common.CustomDataType;
import com.college.collegemanagementsystem.common.UtilityFunctions;
import com.college.collegemanagementsystem.dto.request.create.CreateStudentRequest;
import com.college.collegemanagementsystem.dto.request.update.UpdateStudentRequest;
import com.college.collegemanagementsystem.exception.entityalreadyexists.StudentAlreadyExistsException;
import com.college.collegemanagementsystem.exception.entitynotfound.DepartmentNotFoundException;
import com.college.collegemanagementsystem.exception.entitynotfound.StudentNotFoundException;
import com.college.collegemanagementsystem.model.Student;
import com.college.collegemanagementsystem.model.Subject;
import com.college.collegemanagementsystem.repository.StudentRepository;
import com.college.collegemanagementsystem.service.common.ModelProvider;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final ModelProvider modelProvider;
    private final UtilityFunctions utilityFunctions;

    public StudentService(StudentRepository studentRepository,
                          ModelProvider modelProvider,
                          UtilityFunctions utilityFunctions) {
        this.studentRepository = studentRepository;
        this.modelProvider = modelProvider;
        this.utilityFunctions = utilityFunctions;
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public List<Student> getStudents(String departmentCode) {
        var department = modelProvider.getDepartment(departmentCode);
        var departmentId = department.getId();
        return studentRepository.findAllByDepartmentId(departmentId);
    }

    public Student createStudent(CreateStudentRequest request) {

        var department = modelProvider.getDepartment(request.getDepartmentCode());
        if(department == null) throw new DepartmentNotFoundException(request.getDepartmentCode());

        var student = Student.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .rollNumber(
                        utilityFunctions.generateStudentRollNumber(
                                request.getFirstname(),
                                request.getLastname(),
                                request.getDepartmentCode(),
                                department.getStudents().size()
                        )
                )
                .department(department)
                .build();

        return studentRepository.save(student);
    }

    public Student getStudent(String rollNumber) {
        return studentRepository.findByRollNumber(rollNumber)
                .orElseThrow(() -> new StudentNotFoundException(rollNumber));
    }

    @Transactional
    public CustomDataType updateStudent(String rollNumber, UpdateStudentRequest request) {
        var student = studentRepository.findByRollNumber(rollNumber)
                .orElseThrow(() -> new StudentNotFoundException(rollNumber));

        List<String> noSubjectsFound = new ArrayList<>();

        if (request.getFirstname() != null) {
            student.setFirstname(request.getFirstname());
        }
        if (request.getLastname() != null) {
            student.setLastname(request.getLastname());
        }
        if (request.getSubjects() != null) {
            List<Subject> subjects = student.getSubjectsPassed();
            for (String subjectCode : request.getSubjects()) {
                Subject subject = modelProvider.getSubject(subjectCode, student.getDepartment().getId());
                if(subject == null){
                    noSubjectsFound.add(subjectCode);
                }
                if (subject != null && !subjects.contains(subject)) {
                    subjects.add(subject);
                }
            }
            student.setSubjectsPassed(subjects);
        }

        return CustomDataType.builder()
                .stringList(noSubjectsFound)
                .object(studentRepository.save(student))
                .build();
    }

    public boolean deleteStudent(String rollNumber) {
        try {
            var student = studentRepository.findByRollNumber(rollNumber)
                    .orElseThrow(() -> new StudentNotFoundException(rollNumber));
            studentRepository.delete(student);
            return true;
        } catch (StudentNotFoundException ex) {
            return false;
        }
    }
}

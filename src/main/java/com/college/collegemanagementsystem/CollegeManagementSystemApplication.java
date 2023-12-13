package com.college.collegemanagementsystem;

import com.college.collegemanagementsystem.dto.request.update.UpdateProfessorRequest;
import com.college.collegemanagementsystem.model.Subject;
import com.college.collegemanagementsystem.repository.SubjectRepository;
import com.college.collegemanagementsystem.service.ProfessorService;
import com.college.collegemanagementsystem.service.common.ModelProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class CollegeManagementSystemApplication implements CommandLineRunner {

	@Autowired
	private ProfessorService professorService;

	public static void main(String[] args) {
		SpringApplication.run(CollegeManagementSystemApplication.class, args);
	}

	@Override
//	@Transactional
	public void run(String... args) throws Exception {
//		UpdateProfessorRequest request = new UpdateProfessorRequest();
//		request.setSubjects(List.of("CL"));
//		professorService.updateProfessor("PR@AJCS001", request);
	}
}

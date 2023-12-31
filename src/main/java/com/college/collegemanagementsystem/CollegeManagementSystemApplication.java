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
public class CollegeManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(CollegeManagementSystemApplication.class, args);
	}

}

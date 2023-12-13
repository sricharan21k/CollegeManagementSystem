package com.college.collegemanagementsystem;

import com.college.collegemanagementsystem.dto.request.create.CreateDepartmentRequest;
import com.college.collegemanagementsystem.dto.request.update.UpdateProfessorRequest;
import com.college.collegemanagementsystem.dto.response.create.CreateDepartmentResponse;
import com.college.collegemanagementsystem.dto.response.fetch.FetchCompleteDepartmentResponse;
import com.college.collegemanagementsystem.dto.response.fetch.FetchCompleteProfessorResponse;
import com.college.collegemanagementsystem.dto.response.fetch.FetchPartialDepartmentResponse;
import com.college.collegemanagementsystem.model.Department;
import com.college.collegemanagementsystem.model.Professor;
import com.college.collegemanagementsystem.model.Subject;
import com.college.collegemanagementsystem.repository.DepartmentRepository;
import com.college.collegemanagementsystem.repository.ProfessorRepository;
import com.college.collegemanagementsystem.repository.SubjectRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.WebClientRestTemplateAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.event.annotation.BeforeTestMethod;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import static org.assertj.core.api.Assertions.as;
import static org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder.*;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CollegeManagementSystemApplicationTests {
    @LocalServerPort
    private int port;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    private final String baseUrl = "http://localhost:";

    @BeforeEach
    public void departmentRepo() {
    }

    @Test
    public void testCreateDepartment() {
        String url = baseUrl + port + "/departments";

        CreateDepartmentRequest requestBody = new CreateDepartmentRequest();
        requestBody.setName("Computer Science");

        ResponseEntity<CreateDepartmentResponse> response = restTemplate.exchange(
                url, HttpMethod.POST, new HttpEntity<>(requestBody), CreateDepartmentResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode().value()).isEqualTo(201);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo("CS");
    }


    @Test
    public void testGetDepartments() {
        Professor professor = Professor.builder().firstname("Alex").lastname("Jones").professorId("AJ123").build();
        professorRepository.save(professor);

        Department department1 = Department.builder().name("Computer Science").code("CS").hod(professor).build();
        Department department2 = Department.builder().name("Information Technology").code("IT").build();
        departmentRepository.save(department1);
        departmentRepository.save(department2);

        String url = baseUrl + port + "/departments";

        ResponseEntity<FetchPartialDepartmentResponse[]> response = restTemplate
                .getForEntity(url, FetchPartialDepartmentResponse[].class);

        FetchPartialDepartmentResponse[] responseBody = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseBody).isNotNull();
        assertThat(responseBody).hasSize(2);
    }

    @Test
    @DisplayName("Update Professor")
    public void shouldAddSubjectToProfessor_WhenMultipleSubjectsFound_WithSameCode() throws Exception {
        Department cs = Department.builder().id(1001L).name("Computer Science").code("CS").build();
        Department it = Department.builder().id(1002L).name("Information Technology").code("IT").build();
        departmentRepository.save(cs);
        departmentRepository.save(it);

        Professor professor = Professor.builder().firstname("Adam").lastname("Smith")
                .professorId("AS123").id(2001L).department(cs).build();
        professorRepository.save(professor);

        Subject subject1 = Subject.builder().id(3001L).name("C Language").code("CL").credits(3).department(cs).build();
        Subject subject2 = Subject.builder().id(3002L).name("Java Programming").code("JP").credits(3).department(cs).build();
        Subject subject3 = Subject.builder().id(3003L).name("Computer Lab").code("CL").credits(2).department(it).build();
        subjectRepository.save(subject1);
        subjectRepository.save(subject2);
        subjectRepository.save(subject3);

        String url = baseUrl + port + "/professors/AS123";
        UpdateProfessorRequest request = new UpdateProfessorRequest();
        request.setSubjects(List.of("CL", "JP"));

        FetchCompleteProfessorResponse response = WebClient.create().patch().uri(url)
                .body(BodyInserters.fromValue(request)).retrieve().bodyToMono(FetchCompleteProfessorResponse.class).block();

        assertThat(response).isNotNull();
        assertThat(response.getProfessorId()).isEqualTo("AS123");
        assertThat(response.getName()).isEqualTo("Adam Smith");
        assertThat(response.getSubjectsTaught()).isNotEmpty().hasSize(2).containsExactlyInAnyOrder("CL", "JP");

    }

}

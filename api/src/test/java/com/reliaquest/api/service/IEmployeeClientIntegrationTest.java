package com.reliaquest.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reliaquest.api.dto.EmployeeDtoCreateRequest;
import com.reliaquest.api.dto.EmployeeDtoResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class IEmployeeClientIntegrationTest {

    private IEmployeeClient employeeClient;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        WebClient webClient = WebClient.builder().baseUrl("http://localhost:" + 8112).build();
        employeeClient = new IEmployeeClient(webClient, objectMapper);
    }

    @Test
    void testGetAllEmployees() {
        List<EmployeeDtoResponse> allEmployees = employeeClient.getAllEmployees();
        //Mock server sets up 50 employees
        assertThat(allEmployees.size(), equalTo(50));
    }

    @Test
    void testCreateAndGetAndDeleteEmployee() {
        // Create employee
        EmployeeDtoCreateRequest createRequest = new EmployeeDtoCreateRequest();
        UUID uuid = UUID.randomUUID();

        createRequest.setId(uuid);
        createRequest.setName("Sam C");
        createRequest.setAge(30);
        //Seems to be a bug whereby the email is not created properly
        createRequest.setEmail("Sam345345@hotmail.com");
        createRequest.setSalary(50000);
        createRequest.setTitle("Developer");

        EmployeeDtoResponse createdEmployee = employeeClient.createEmployee(createRequest);
        assertNotNull(createdEmployee);
        assertEquals("Sam C", createdEmployee.getEmployeeName());

        // Get employee by ID
        EmployeeDtoResponse fetchedEmployee = employeeClient.getEmployeeById(createdEmployee.getId().toString());
        assertEquals("Sam C", fetchedEmployee.getEmployeeName());

        // Delete employee
        Boolean deleted = employeeClient.deleteEmployee("Sam C");
        assertTrue(deleted);
    }

    @Configuration
    static class TestConfig {
        @Bean
        public ObjectMapper objectMapper() {
            return new ObjectMapper();
        }
    }
}

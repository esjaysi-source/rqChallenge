package com.reliaquest.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reliaquest.api.dto.EmployeeDtoCreateRequest;
import com.reliaquest.api.dto.EmployeeDtoDeleteRequest;
import com.reliaquest.api.dto.EmployeeDtoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@Slf4j
public class IEmployeeClient {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public IEmployeeClient(WebClient webClient, ObjectMapper objectMapper) {
        this.webClient = webClient;
        this.objectMapper = objectMapper;
    }

    public List<EmployeeDtoResponse> getAllEmployees() {
        log.info("Retrieving all employee data");
        String responseBody = webClient.get()
                .uri("http://localhost:8112/api/v1/employee")
                .retrieve()
                .bodyToMono(String.class)
                .block();
        try {
            JsonNode root = objectMapper.readTree(responseBody);
            JsonNode dataNode = root.get("data");

            if (dataNode == null || !dataNode.isArray()) {
                throw new RuntimeException("Expected 'data' array in JSON response, but got: " + root);
            }

            return objectMapper.convertValue(dataNode, new TypeReference<>() {
            });

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse employees JSON", e);
        }
    }

    public EmployeeDtoResponse getEmployeeById(String id) {
        String responseBody = webClient.get()
                .uri("http://localhost:8112/api/v1/employee/{id}", id)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        try {
            JsonNode root = objectMapper.readTree(responseBody);
            JsonNode dataNode = root.get("data");

            if (dataNode == null || !dataNode.isObject()) {
                throw new RuntimeException("Expected 'data' object in JSON response, but got: " + root);
            }

            return objectMapper.convertValue(dataNode, EmployeeDtoResponse.class);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse employee JSON for ID: " + id, e);
        }
    }

    public EmployeeDtoResponse createEmployee(EmployeeDtoCreateRequest input) {
        String responseBody = webClient.post()
                .uri("http://localhost:8112/api/v1/employee")
                .bodyValue(input)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        try {
            JsonNode root = objectMapper.readTree(responseBody);
            JsonNode dataNode = root.get("data");

            if (dataNode == null || !dataNode.isObject()) {
                throw new RuntimeException("Expected 'data' object in JSON response, but got: " + root);
            }

            return objectMapper.convertValue(dataNode, EmployeeDtoResponse.class);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse created employee JSON", e);
        }
    }

    public Boolean deleteEmployee(String name) {
        EmployeeDtoDeleteRequest input = new EmployeeDtoDeleteRequest();
        input.setName(name);

        String responseBody = webClient.method(HttpMethod.DELETE)
                .uri("http://localhost:8112/api/v1/employee")
                .bodyValue(input)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        try {
            JsonNode root = objectMapper.readTree(responseBody);
            JsonNode dataNode = root.get("data");

            if (dataNode == null || !dataNode.isBoolean()) {
                throw new RuntimeException("Expected 'data' boolean in JSON response, but got: " + root);
            }

            return dataNode.asBoolean();

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse delete employee JSON", e);
        }
    }

}

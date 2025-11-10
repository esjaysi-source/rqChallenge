package com.reliaquest.api.service;

import com.reliaquest.api.dto.EmployeeDtoCreateRequest;
import com.reliaquest.api.dto.EmployeeDtoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class IEmployeeService {

    private final IEmployeeClient employeeClient;

    public IEmployeeService(IEmployeeClient employeeClient) {
        this.employeeClient = employeeClient;
    }

    public List<EmployeeDtoResponse> getAllEmployees() {
        return employeeClient.getAllEmployees();
    }

    public EmployeeDtoResponse getEmployeeById(String id) {
        return employeeClient.getEmployeeById(id);
    }

    public EmployeeDtoResponse createEmployee(EmployeeDtoCreateRequest employeeDtoCreateRequest) {
        return employeeClient.createEmployee(employeeDtoCreateRequest);
    }

    public Boolean deleteEmployee(String id) {
        EmployeeDtoResponse employeeDtoResponse = getEmployeeById(id);
        String employeeName = employeeDtoResponse.getEmployeeName();

        return employeeClient.deleteEmployee(employeeName);
    }

    public List<EmployeeDtoResponse> filterEmployeesByNameSearch(String searchString) {

        List<EmployeeDtoResponse> allEmployees = getAllEmployees();

        return allEmployees.stream().filter(
                employee -> employee.getEmployeeName().toLowerCase().contains(searchString.toLowerCase())
        ).toList();
    }

    public Integer getHighestSalaryOfEmployees() {

        List<EmployeeDtoResponse> allEmployees = getAllEmployees();

        Optional<Integer> highestSalaryOfEmployees = allEmployees.stream()
                .filter(e -> e.getEmployeeSalary() != null)
                .max(Comparator.comparing(EmployeeDtoResponse::getEmployeeSalary))
                .map(EmployeeDtoResponse::getEmployeeSalary).stream().findFirst();

        if (highestSalaryOfEmployees.isPresent()) {
            return highestSalaryOfEmployees.get();
        } else {
            log.error("Error retrieving highest salary of employees");
            throw new RuntimeException("Error retrieving highest salary of employees");
        }
    }

    public List<String> getTopTenHighestEarningEmployeeNames() {

        List<EmployeeDtoResponse> allEmployees = getAllEmployees();

        return allEmployees.stream()
                .filter(e -> e.getEmployeeSalary() != null)
                .sorted(Comparator.comparing(EmployeeDtoResponse::getEmployeeSalary).reversed())
                .limit(10)
                .map(EmployeeDtoResponse::getEmployeeName)
                .toList();
    }

}

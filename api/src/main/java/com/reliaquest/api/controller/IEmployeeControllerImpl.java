package com.reliaquest.api.controller;

import com.reliaquest.api.dto.EmployeeDtoCreateRequest;
import com.reliaquest.api.dto.EmployeeDtoResponse;
import com.reliaquest.api.service.IEmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class IEmployeeControllerImpl implements IEmployeeController<EmployeeDtoResponse, EmployeeDtoCreateRequest> {

    private final IEmployeeService employeeService;

    public IEmployeeControllerImpl(IEmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    public ResponseEntity<List<EmployeeDtoResponse>> getAllEmployees() {
        List<EmployeeDtoResponse> employees = employeeService.getAllEmployees();

        log.info("getAllEmployees, number of employees = {} ", employees.size());

        return ResponseEntity.ok(employees);
    }

    @Override
    public ResponseEntity<List<EmployeeDtoResponse>> getEmployeesByNameSearch(String searchString) {
        List<EmployeeDtoResponse> filteredEmployees = employeeService.filterEmployeesByNameSearch(searchString);

        log.info("getEmployeesByNameSearch {} employees matched = {} ", searchString, filteredEmployees.size());

        return ResponseEntity.ok(filteredEmployees);
    }

    @Override
    public ResponseEntity<EmployeeDtoResponse> getEmployeeById(String id) {
        EmployeeDtoResponse employeeDtoResponse = employeeService.getEmployeeById(id);

        log.info("getEmployeeById, employee id = {} ", id);

        return ResponseEntity.ok(employeeDtoResponse);
    }

    @Override
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        Integer highestSalary = employeeService.getHighestSalaryOfEmployees();

        log.info("Returning highest salary = {} ", highestSalary);

        return ResponseEntity.ok(highestSalary);
    }

    @Override
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        List<String> employeeNames = employeeService.getTopTenHighestEarningEmployeeNames();

        log.info("getTopTenHighestEarningEmployeeNames, {}", employeeNames.size());

        return ResponseEntity.ok(employeeNames);
    }

    @Override
    public ResponseEntity<EmployeeDtoResponse> createEmployee(EmployeeDtoCreateRequest employeeInput) {

        EmployeeDtoResponse employeeDtoResponse = employeeService.createEmployee(employeeInput);

        log.info("createdEmployee = {} ", employeeDtoResponse.getEmployeeName());

        return ResponseEntity.ok(employeeDtoResponse);
    }


    @Override
    public ResponseEntity<String> deleteEmployeeById(String id) {
        String response = String.valueOf(employeeService.deleteEmployee(id));

        log.info("deleteEmployeeById, employee deleted = {} ", response);

        return ResponseEntity.ok(response);
    }
}

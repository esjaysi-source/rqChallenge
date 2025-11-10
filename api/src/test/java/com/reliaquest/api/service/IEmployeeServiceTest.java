package com.reliaquest.api.service;

import com.reliaquest.api.dto.EmployeeDtoCreateRequest;
import com.reliaquest.api.dto.EmployeeDtoResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class IEmployeeServiceTest {

    private IEmployeeClient employeeClient;
    private IEmployeeService employeeService;

    @BeforeEach
    void setUp() {
        employeeClient = mock(IEmployeeClient.class);
        employeeService = new IEmployeeService(employeeClient);
    }

    @Test
    void testFilterEmployeesByNameSearch() {
        List<EmployeeDtoResponse> mockEmployees = List.of(
                new EmployeeDtoResponse(UUID.randomUUID(), "Alice", 1000, 25, "Dev", "a@a.com"),
                new EmployeeDtoResponse(UUID.randomUUID(), "Bob", 2000, 30, "QA", "b@b.com")
        );

        when(employeeClient.getAllEmployees()).thenReturn(mockEmployees);

        List<EmployeeDtoResponse> result = employeeService.filterEmployeesByNameSearch("bob");
        assertEquals(1, result.size());
        assertEquals("Bob", result.get(0).getEmployeeName());
    }

    @Test
    void testGetHighestSalaryOfEmployees() {
        List<EmployeeDtoResponse> mockEmployees = List.of(
                new EmployeeDtoResponse(UUID.randomUUID(), "Alice", 1000, 25, "Dev", "a@a.com"),
                new EmployeeDtoResponse(UUID.randomUUID(), "Bob", 5000, 30, "QA", "b@b.com")
        );

        when(employeeClient.getAllEmployees()).thenReturn(mockEmployees);

        Integer highestSalary = employeeService.getHighestSalaryOfEmployees();
        assertEquals(5000, highestSalary);
    }

    @Test
    void testGetTopTenHighestEarningEmployeeNames() {
        List<EmployeeDtoResponse> mockEmployees = List.of(
                new EmployeeDtoResponse(UUID.randomUUID(), "A", 100, 25, "Dev", "a@a.com"),
                new EmployeeDtoResponse(UUID.randomUUID(), "B", 200, 25, "Dev", "b@b.com"),
                new EmployeeDtoResponse(UUID.randomUUID(), "C", 300, 25, "Dev", "c@c.com")
        );

        when(employeeClient.getAllEmployees()).thenReturn(mockEmployees);

        List<String> topNames = employeeService.getTopTenHighestEarningEmployeeNames();
        assertEquals(List.of("C", "B", "A"), topNames);
    }
}

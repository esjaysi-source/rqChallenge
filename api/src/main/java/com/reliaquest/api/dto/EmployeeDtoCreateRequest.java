package com.reliaquest.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDtoCreateRequest {

    private UUID id;

    @NotBlank(message = "Employee name must not be blank")
    @JsonProperty("name")
    private String name;

    @NotNull(message = "Employee salary must not be null")
    @JsonProperty("salary")
    private Integer salary;

    @NotNull(message = "Employee age must not be null")
    @JsonProperty("age")
    private Integer age;

    @NotBlank(message = "Employee title must not be blank")
    @JsonProperty("title")
    private String title;

    @NotBlank(message = "Employee email must not be blank")
    @JsonProperty("email")
    private String email;

}
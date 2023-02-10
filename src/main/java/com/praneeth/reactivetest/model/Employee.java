package com.praneeth.reactivetest.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Employee {
    @NotNull
    @Size(max = 5)
    private String id;

    @NotNull
    @Size(max = 25)
    private String firstName;

    @NotNull
    @Size(max = 25)
    private String role;

}

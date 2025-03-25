package com.sangyunpark.springbatchpractice.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class Customer {
    private String id;
    private String name;
    private String email;
    private LocalDateTime registeredDate;
}

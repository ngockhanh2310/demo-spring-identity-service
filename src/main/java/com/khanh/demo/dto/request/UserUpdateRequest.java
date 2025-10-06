package com.khanh.demo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserUpdateRequest {
    private String password;
    private String firstName;
    private String lastName;
    private LocalDate dob;
}

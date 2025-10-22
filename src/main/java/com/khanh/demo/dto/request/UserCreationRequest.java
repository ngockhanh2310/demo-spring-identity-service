package com.khanh.demo.dto.request;

import com.khanh.demo.validator.DobConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    @Size(min = 6, max = 20, message = "USERNAME_INVALID")
    @NotNull(message = "Username is required")
    private String username;
    @Size(min = 8, max = 20, message = "PASSWORD_INVALID")
    private String password;
    private String firstName;
    private String lastName;
    @DobConstraint(min = 16, message = "INVALID_DOB")
    private LocalDate dob;
}

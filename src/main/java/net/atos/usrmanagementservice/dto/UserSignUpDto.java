package net.atos.usrmanagementservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class UserSignUpDto {

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    String email;

    @NotBlank(message = "Password is mandatory")
    String password;

    @NotBlank(message = "First name is mandatory")
    String firstName;

    @NotBlank(message = "Last name is mandatory")
    String lastName;

    @NotBlank(message = "National ID is mandatory")
    String nationalid;
}

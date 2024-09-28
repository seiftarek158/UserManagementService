package net.atos.usrmanagementservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDto {
    String authToken;
    String message;
    String status;

}

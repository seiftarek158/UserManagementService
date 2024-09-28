package net.atos.usrmanagementservice.controller;

import jakarta.validation.Valid;
import net.atos.usrmanagementservice.dto.LoginResponseDto;
import net.atos.usrmanagementservice.dto.UserLoginDto;
import net.atos.usrmanagementservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class LoginController {

    @Autowired
    UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody UserLoginDto userLoginDto) {
        Optional<String> token = userService.verify(userLoginDto);
        if (token.isPresent()) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + token.get());

            return ResponseEntity.status(200).headers(headers).body(new LoginResponseDto(token.get(),"Login Successful","200"));
//            return ResponseEntity.status(200).headers(headers).build();
        } else {

            return ResponseEntity.status(500).body(new LoginResponseDto("","Login Failed","500"));
        }
    }


}

package net.atos.usrmanagementservice.controller;

import jakarta.validation.Valid;
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
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody UserLoginDto userLoginDto) {
        Optional<String> token = userService.verify(userLoginDto);
        Map<String, String> response = new HashMap<>();
        if (token.isPresent()) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + token.get());

            response.put("message", "Login successful");
            response.put("status", "200");
            response.put("authToken", token.get());
//            response.put("expiry", userService.getTokenExpiryDate(token.get()).toString());
            return ResponseEntity.status(200).headers(headers).body(response);
        } else {
            response.put("message", "Invalid credentials");
            response.put("status", "500");
            return ResponseEntity.status(500).body(response);
        }
    }


}

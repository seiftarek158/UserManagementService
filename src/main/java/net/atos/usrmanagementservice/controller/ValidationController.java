package net.atos.usrmanagementservice.controller;

import net.atos.usrmanagementservice.service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ValidationController {

    @Autowired
    JWTService jwtService;


    @PostMapping("/validation")
    public ResponseEntity<Boolean> validateToken(@RequestBody String token) {
        Map<String, String> response = new HashMap<>();
        return ResponseEntity.ok(jwtService.validateToken(token));
    }

    @PostMapping("/nationalId")
    public ResponseEntity<String> extractNationalid(@RequestBody String token) {


        return ResponseEntity.ok(jwtService.extractNationalid(token));
    }





}

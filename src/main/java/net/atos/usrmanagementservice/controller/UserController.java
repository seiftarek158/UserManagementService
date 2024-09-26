package net.atos.usrmanagementservice.controller;

import jakarta.validation.Valid;
import net.atos.usrmanagementservice.dto.UserSignUpDto;
import net.atos.usrmanagementservice.model.User;
import net.atos.usrmanagementservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("Home")
public class UserController {

    @Autowired
    UserService userService;


    @PostMapping("/users")
    public ResponseEntity<UserSignUpDto> register(@Valid @RequestBody UserSignUpDto user) {
        UserSignUpDto userSignUpDto= userService.register(user);
        return ResponseEntity.ok(userSignUpDto);
    }





}

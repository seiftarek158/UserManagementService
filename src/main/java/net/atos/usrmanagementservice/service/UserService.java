//package net.atos.dms.service;
//
//import jakarta.annotation.PostConstruct;
//import net.atos.dms.dto.UserLoginDto;
//import net.atos.dms.dto.UserSignUpDto;
//import net.atos.dms.exception.DuplicateEntryException;
//import net.atos.dms.model.User;
//import net.atos.dms.repo.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Service;
//import net.atos.dms.mappers.toUserSignUpDto;
//
//
//import java.time.LocalDateTime;
//import java.util.Date;
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class UserService {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    @Autowired
//    private JWTService jwtService;
//
//
//    @Autowired
//    private toUserSignUpDto toUserSignUpDto;
//
//    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(14);
//
//
//    public User getUserByEmail(String email) {
//        return userRepository.findByEmail(email);
//    }
//
//    public List<User> getAllUsers() {
//        return userRepository.findAll();
//    }
//
//
//    // This method is used to register a new user through a dto and save it to the User table in database
//    public UserSignUpDto register(UserSignUpDto user) {
//        if (userRepository.findByEmail(user.getEmail()) != null) {
//            throw new DuplicateEntryException("Email already exists");
//        }
//        if (userRepository.findByNationalid(user.getNationalid()) != null) {
//            throw new DuplicateEntryException("National ID already exists");
//        }
//        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
//        User savedUser= toUserSignUpDto.toEntity(user);
//        userRepository.save(savedUser);
//        return toUserSignUpDto.toDto(savedUser);
//    }
//
//    @PostConstruct
//    public void initAdminUser() {
//        if (userRepository.findByEmail("admin@gmail.com") == null) {
//            User user = new User();
//            user.setEmail("admin@gmail.com");
//            user.setPassword(bCryptPasswordEncoder.encode("password"));
//            user.setFirstName("Admin");
//            user.setLastName("Admin");
//            user.setNationalid("12345678901234567890");
//            userRepository.save(user);
//        }
//    }
//
//    public Optional<String> verify(UserLoginDto userLoginDto){
//        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLoginDto.getEmail(), userLoginDto.getPassword()));
//
//        if(authentication.isAuthenticated()) {
//            return Optional.ofNullable(jwtService.generateToken(userLoginDto.getEmail()));
//        }
//        return Optional.empty();
//
//    }
//
//    public Date getTokenExpiryDate(String s) {
//        return jwtService.extractExpiration(s);
//    }
//}
//
package net.atos.usrmanagementservice.service;

import jakarta.annotation.PostConstruct;
import net.atos.usrmanagementservice.dto.UserLoginDto;
import net.atos.usrmanagementservice.dto.UserSignUpDto;
import net.atos.usrmanagementservice.mappers.toUserSignUpDto;
import net.atos.usrmanagementservice.model.User;
import net.atos.usrmanagementservice.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTService jwtService;


    @Autowired
    private toUserSignUpDto toUserSignUpDto;

    @Value("${user.default.directory}")
    private String defaultDirectory;

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(14);


    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public String getNationalidByEmail(String email) {
        return userRepository.findByEmail(email).getNationalid();
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    // This method is used to register a new user through a dto and save it to the User table in database
    public UserSignUpDto register(UserSignUpDto user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        User savedUser= toUserSignUpDto.toEntity(user);
        userRepository.save(savedUser);

        createUserDirectory(savedUser.getNationalid());
        return toUserSignUpDto.toDto(savedUser);
    }
    private void createUserDirectory(String nationalid) {
        String directoryPath = defaultDirectory + "/" + nationalid;
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            boolean isCreated= directory.mkdirs();
            if (!isCreated) {
                throw new RuntimeException("Failed to create directory for user: " + nationalid);
            }
        }
    }

    @PostConstruct
    public void initAdminUser() {
        if (userRepository.findByEmail("admin@gmail.com") == null) {
            User user = new User();
            user.setEmail("admin@gmail.com");
            user.setPassword(bCryptPasswordEncoder.encode("password"));
            user.setFirstName("Admin");
            user.setLastName("Admin");
            user.setNationalid("12345678901234567890");
            userRepository.save(user);
            createUserDirectory(user.getNationalid());
        }
    }

    public Optional<String> verify(UserLoginDto userLoginDto){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLoginDto.getEmail(), userLoginDto.getPassword()));
        if(authentication.isAuthenticated()) {
            return Optional.ofNullable(jwtService.generateToken(userLoginDto.getEmail(),getNationalidByEmail(userLoginDto.getEmail())));
        }
        return Optional.empty();

    }
}
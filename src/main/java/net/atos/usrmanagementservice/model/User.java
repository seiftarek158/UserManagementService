package net.atos.usrmanagementservice.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity(name = "users")
@Data
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    long id;

    @Column(unique = true, nullable = false)
    String email;
    @Column(nullable = false)
    String password;

    @Column(name = "first_name", nullable = false)
    String firstName;

    @Column(name = "last_name", nullable = false)
    String lastName;

    @Column(name = "national_id", nullable = false)
    String nationalid;

    @CreatedDate
    @Column(name = "sign_in_at", nullable = false , updatable = false)
    LocalDateTime signInAt;








}

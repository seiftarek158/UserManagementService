package net.atos.usrmanagementservice.repo;

import net.atos.usrmanagementservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmail(String email);
    User findByNationalid(String nationalid);
}

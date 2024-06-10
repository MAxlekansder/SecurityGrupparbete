package org.example.securitygrupparbete.Repository;

import org.apache.catalina.User;
import org.example.securitygrupparbete.Model.UserAuthentication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<UserAuthentication, Long> {

    Optional<UserAuthentication> findByUsername(String username);
}

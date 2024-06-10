package org.example.securitygrupparbete.Repository;

import org.example.securitygrupparbete.Model.UserAuthentication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<UserAuthentication, Long> {

}

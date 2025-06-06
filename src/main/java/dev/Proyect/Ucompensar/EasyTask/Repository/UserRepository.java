package dev.Proyect.Ucompensar.EasyTask.Repository;

import dev.Proyect.Ucompensar.EasyTask.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByPhoneNumber(String phoneNumber);
}

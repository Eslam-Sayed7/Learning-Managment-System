package com.Java.LMS.platform.infrastructure.repository;
import com.Java.LMS.platform.domain.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Override
    Optional<User> findById(Long userId);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}

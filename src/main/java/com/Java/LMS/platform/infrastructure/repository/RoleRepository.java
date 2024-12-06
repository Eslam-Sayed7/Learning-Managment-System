package com.Java.LMS.platform.infrastructure.repository;
import com.Java.LMS.platform.domain.Entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRoleName(String roleName);
}

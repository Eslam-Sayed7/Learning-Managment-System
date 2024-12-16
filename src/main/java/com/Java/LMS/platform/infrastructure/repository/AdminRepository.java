package com.Java.LMS.platform.infrastructure.repository;

import com.Java.LMS.platform.domain.Entities.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    @Override
    Optional<Admin> findById(Long aLong);
}

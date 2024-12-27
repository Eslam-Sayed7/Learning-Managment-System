package com.Java.LMS.platform.infrastructure.repository;

import com.Java.LMS.platform.domain.Entities.Choice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChoiceRepository extends JpaRepository<Choice, Long> {

    Optional<Choice> findById(Long questionId);
}

package com.Java.LMS.platform.service.impl;

import com.Java.LMS.platform.domain.Entities.User;
import com.Java.LMS.platform.domain.Entities.Role;
import com.Java.LMS.platform.domain.Entities.Student;
import com.Java.LMS.platform.domain.Entities.Admin;
import com.Java.LMS.platform.domain.Entities.Instructor;
import com.Java.LMS.platform.infrastructure.repository.RoleRepository;
import com.Java.LMS.platform.infrastructure.repository.UserRepository;
import com.Java.LMS.platform.infrastructure.repository.AdminRepository;
import com.Java.LMS.platform.infrastructure.repository.InstructorRepository;
import com.Java.LMS.platform.infrastructure.repository.StudentRepository;
import com.Java.LMS.platform.service.UserService;
import com.Java.LMS.platform.service.dto.RegisterRequestModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final StudentRepository studentRepository;
    private final InstructorRepository instructorRepository;
    private final AdminRepository adminRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder,
                       StudentRepository studentRepository, InstructorRepository instructorRepository,
                       AdminRepository adminRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.studentRepository = studentRepository;
        this.instructorRepository = instructorRepository;
        this.adminRepository = adminRepository;
    }

    @Transactional
    public void registerUserAndSyncRole(RegisterRequestModel registerDto) {

        if (userRepository.findByUsername(registerDto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username is already taken!");
        }

        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setEmail(registerDto.getEmail());

        Role role = roleRepository.findByRoleName(registerDto.getRoleName());
        if (role == null) {
            throw new IllegalArgumentException("Invalid role!");
        }
        user.setRole(role);
        userRepository.save(user);

        switch (registerDto.getRoleName().toUpperCase()) {
            case "ROLE_STUDENT":
                syncStudent(user, registerDto);
                break;
            case "ROLE_INSTRUCTOR":
                syncInstructor(user, registerDto);
                break;
            case "ROLE_ADMIN":
                syncAdmin(user, registerDto);
                break;
            default:
                throw new IllegalArgumentException("Unsupported role type!");
        }
    }

    private void syncStudent(User user, RegisterRequestModel registerDto) {
        Student student = new Student();
        student.setUserId(user.getUserId());
        // Create a valid JSON string
        String additionalInfo = "{\"key\": \"info\"}";
        student.setAdditionalInfo(additionalInfo);

        student.setMajor("MAJOR");
        student.setYearOfStudy(3);

        studentRepository.save(student);
    }



    private void syncInstructor(User user, RegisterRequestModel registerDto) {
        Instructor instructor = new Instructor();
        instructor.setUser(user);
//        instructor.setDepartment(registerDto.getDepartment());
//        instructor.setTitle(registerDto.getTitle());
        instructorRepository.save(instructor);
    }

    private void syncAdmin(User user, RegisterRequestModel registerDto) {
        Admin admin = new Admin();
        admin.setUser(user);
//        admin.setAccessLevel(registerDto.getAccessLevel());
        adminRepository.save(admin);
    }
}


package com.Java.LMS.platform.service.impl;

import com.Java.LMS.platform.domain.Entities.*;
import com.Java.LMS.platform.infrastructure.repository.*;
import com.Java.LMS.platform.service.UserService;
import com.Java.LMS.platform.service.dto.Auth.AuthServiceResult;
import com.Java.LMS.platform.service.dto.Auth.RegisterRequestModel;
import org.apache.logging.log4j.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public AuthServiceResult registerUserAndSyncRole(RegisterRequestModel registerDto) {
        var result = new AuthServiceResult();
        try {
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
            boolean isSynced = false;
            String message;
            switch (registerDto.getRoleName().toUpperCase()) {
                case "ROLE_STUDENT":
                    isSynced = syncStudent(user, registerDto);
                    message = "Registered as student";
                    break;
                case "ROLE_INSTRUCTOR":
                    isSynced = syncInstructor(user, registerDto);
                    message = "Registered as instructor";
                    break;
                case "ROLE_ADMIN":
                    isSynced = syncAdmin(user, registerDto);
                    message = "Registered as Admin";
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported role type!");
            }
            result.setMessage(message);
            result.setResultState(isSynced);
            return result;
        } catch (Exception e) {
            result.setMessage(e.getMessage());
            result.setResultState(false);
            return result;
        }
    }

    private boolean syncStudent(User user, RegisterRequestModel registerDto) {
        try {
            Student student = new Student();
            student.setUserId(user.getUserId());

            // Create a valid JSON string
            String additionalInfo = "{\"key\": \"info\"}";
            student.setAdditionalInfo(additionalInfo);

            student.setMajor("MAJOR");
            student.setYearOfStudy(3);
            studentRepository.save(student);
            return true;

        } catch (Exception e) {
            System.err.println("Error while registering as student and syncing student: " + e.getMessage());
            return false;
        }
    }
    private boolean syncInstructor(User user, RegisterRequestModel registerDto) {
        try {
            Instructor instructor = new Instructor();
            instructor.setUser(user);

            // instructor.setDepartment(registerDto.getDepartment());
            // instructor.setTitle(registerDto.getTitle());

            instructorRepository.save(instructor);

            return true;
        } catch (Exception e) {
            System.err.println("Error syncing instructor: " + e.getMessage());
            return false;
        }
    }
    private boolean syncAdmin(User user, RegisterRequestModel registerDto) {
        try {
            Admin admin = new Admin();
            admin.setUser(user);
            // admin.setAccessLevel(registerDto.getAccessLevel());
            adminRepository.save(admin);
            return true;
        } catch (Exception e) {
            System.err.println("Error syncing admin: " + e.getMessage());
            return false;
        }
    }
}
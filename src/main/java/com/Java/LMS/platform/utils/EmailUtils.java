package com.Java.LMS.platform.utils;

import com.Java.LMS.platform.service.dto.Email.EmailFormateDto;

public class EmailUtils {
    public static String getEmailMessage(String name){
        return "Salam " + name + ", \n\nYour new account has been created.";
    }
    public static EmailFormateDto registerationEmail( String to) {
        EmailFormateDto emailFormateDto = new EmailFormateDto();
        emailFormateDto.setTo(to);
        emailFormateDto.setSubject("New User Account Verification");
        emailFormateDto.setEmailBody("Welcome to the LMS as new User");

        return emailFormateDto;
    }
    public static EmailFormateDto CourseActionEmail( String to) {
        EmailFormateDto emailFormateDto = new EmailFormateDto();
        emailFormateDto.setTo(to);
        emailFormateDto.setSubject("New User Account Verification");
        emailFormateDto.setEmailBody("Welcome to the LMS as new User");

        return emailFormateDto;
    }


}

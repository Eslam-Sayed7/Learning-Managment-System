package com.Java.LMS.platform.service;

import com.Java.LMS.platform.service.dto.Email.EmailFormateDto;

public  interface EmailService {
    void sendEmail(EmailFormateDto emailDto);
}

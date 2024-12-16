package com.Java.LMS.platform.service;

import com.Java.LMS.platform.domain.Entities.User;
import com.Java.LMS.platform.service.dto.RegisterRequestModel;

public interface UserService {
    void registerUserAndSyncRole(RegisterRequestModel registerRequestModel);
}

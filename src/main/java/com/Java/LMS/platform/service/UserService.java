package com.Java.LMS.platform.service;

import com.Java.LMS.platform.service.dto.Auth.AuthServiceResult;
import com.Java.LMS.platform.service.dto.Auth.RegisterRequestModel;

public interface UserService {
    AuthServiceResult registerUserAndSyncRole(RegisterRequestModel registerRequestModel);
    // TODO
//    boolean changePassword(ChangePasswordRequestModel changePasswordRequestModel);
//    boolean changeMail(ChangeMailRequestModel changeMailRequestModel);
//    boolean changeUserName(ChangeUserNameRequestModel changeUserNameRequestModel);
}

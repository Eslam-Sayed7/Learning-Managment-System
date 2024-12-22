package com.Java.LMS.platform.service;

import com.Java.LMS.platform.service.dto.Auth.AccountRequestModel;
import com.Java.LMS.platform.service.dto.Auth.AccountResponse;
import com.Java.LMS.platform.service.dto.Auth.AuthServiceResult;
import com.Java.LMS.platform.service.dto.Auth.RegisterRequestModel;

public interface UserService {
    AuthServiceResult registerUserAndSyncRole(RegisterRequestModel registerRequestModel);
    // TODO
    AccountResponse changePassword(AccountRequestModel changePasswordRequestModel);
//    boolean changeMail(ChangeMailRequestModel changeMailRequestModel);
//    boolean changeUserName(ChangeUserNameRequestModel changeUserNameRequestModel);
}

package com.Java.LMS.platform.service;

import com.Java.LMS.platform.service.dto.Auth.*;

public interface UserService {
    AuthServiceResult registerUserAndSyncRole(RegisterRequestModel registerRequestModel);
    // TODO
    AccountResponse changePassword(AccountRePasswordModel changePasswordRequestModel);
    AccountResponse changeMail(AccountReMailModel changeMailRequestModel);
    //AuthServiceResult changeUsername(AccountRequestModel changeUserNameRequestModel);
}

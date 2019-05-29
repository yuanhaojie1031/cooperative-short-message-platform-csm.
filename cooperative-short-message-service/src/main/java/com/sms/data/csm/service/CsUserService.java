package com.sms.data.csm.service;

import com.sms.data.csm.model.CsUser;

import java.util.List;

public interface CsUserService {
    CsUser login(CsUser csUser);
    void insertCsUser(CsUser csUser);
    List<CsUser> selectCsUser();
}

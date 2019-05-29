package com.sms.data.csm.service.impl;

import com.sms.data.csm.mapper.CsUserMapper;
import com.sms.data.csm.model.CsUser;
import com.sms.data.csm.service.CsUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CsUserServiceImpl implements CsUserService {

    private CsUserMapper csUserMapper;

    @Autowired
    public CsUserServiceImpl(CsUserMapper csUserMapper){
        this.csUserMapper = csUserMapper;
    }

    public CsUser login(CsUser csUser){
        return csUserMapper.login(csUser);
    }

    public void insertCsUser(CsUser csUser){
        csUser.setCreateTime(new Date());
        csUserMapper.insertCsUser(csUser);
    }

    public List<CsUser> selectCsUser(){
        return csUserMapper.selectCsUser();
    }
}

package com.sms.data.csm.mapper;

import com.sms.data.csm.model.CsUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CsUserMapper {
    CsUser login(@Param("csUser") CsUser csUser);
    int insertCsUser(@Param("csUser") CsUser csUser);
    List<CsUser> selectCsUser();
}

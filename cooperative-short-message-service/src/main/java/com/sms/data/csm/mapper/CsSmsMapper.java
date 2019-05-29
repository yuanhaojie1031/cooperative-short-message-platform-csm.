package com.sms.data.csm.mapper;

import com.sms.data.csm.model.CsSms;
import org.apache.ibatis.annotations.Param;

import org.springframework.stereotype.Repository;

@Repository
public interface CsSmsMapper {
    int insertCsSms(@Param("csSms") CsSms csSms);
}

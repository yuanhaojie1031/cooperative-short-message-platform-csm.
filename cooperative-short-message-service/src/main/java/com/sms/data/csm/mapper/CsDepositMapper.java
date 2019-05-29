package com.sms.data.csm.mapper;

import com.sms.data.csm.model.CsDeposit;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CsDepositMapper {
    void insertCsDeposit(@Param("csDeposit") CsDeposit csDeposit);
    void updateCsDeposit(@Param("csDeposit") CsDeposit csDeposit);
    CsDeposit selectCsDeposit(@Param("userId") Integer userId);
}

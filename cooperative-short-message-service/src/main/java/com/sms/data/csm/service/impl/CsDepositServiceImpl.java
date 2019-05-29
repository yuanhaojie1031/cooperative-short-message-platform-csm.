package com.sms.data.csm.service.impl;

import com.sms.data.csm.mapper.CsDepositMapper;
import com.sms.data.csm.mapper.CsSmsRecordMapper;
import com.sms.data.csm.model.CsDeposit;
import com.sms.data.csm.model.CsSmsRecord;
import com.sms.data.csm.model.CsUser;
import com.sms.data.csm.service.CsDepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class CsDepositServiceImpl implements CsDepositService {

    private CsDepositMapper csDepositMapper;

    private CsSmsRecordMapper csSmsRecordMapper;

    @Autowired
    public CsDepositServiceImpl(CsDepositMapper csDepositMapper,CsSmsRecordMapper csSmsRecordMapper){
        this.csDepositMapper=csDepositMapper;
        this.csSmsRecordMapper=csSmsRecordMapper;
    }

    @Transactional
    public  void insertCsDeposit(CsDeposit csDeposit){
        int count =  csDepositMapper.insertCsDeposit(csDeposit);
        if (count >0) {
            //插入记录表
            CsSmsRecord csSmsRecord = new CsSmsRecord();
            csSmsRecord.setNumber(csDeposit.getNumber());
            csSmsRecord.setCreateTime(new Date());
            csSmsRecord.setUserId(csDeposit.getUserId());
            csSmsRecordMapper.insertCsSmsRecord(csSmsRecord);
        }
    }
}

package com.sms.data.csm.service.impl;

import com.sms.data.csm.mapper.CsDepositMapper;
import com.sms.data.csm.mapper.CsSmsRecordMapper;
import com.sms.data.csm.model.CsDeposit;
import com.sms.data.csm.model.CsSmsRecord;
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
    public CsDepositServiceImpl(CsDepositMapper csDepositMapper, CsSmsRecordMapper csSmsRecordMapper) {
        this.csDepositMapper = csDepositMapper;
        this.csSmsRecordMapper = csSmsRecordMapper;
    }

    @Transactional
    public int insertCsDeposit(CsDeposit csDeposit) {
        //查询充值记录
        CsDeposit csDepositRes = csDepositMapper.selectCsDeposit(csDeposit.getUserId());
        if (csDepositRes != null) {
            csDeposit.setUpdateTime(new Date());
            csDepositMapper.updateCsDeposit(csDeposit);
        } else {
            csDeposit.setCreateTime(new Date());
            csDepositMapper.insertCsDeposit(csDeposit);
        }
        //设置记录表参数
        CsSmsRecord csSmsRecord = new CsSmsRecord();
        csSmsRecord.setSmsNumber(csDeposit.getSmsNumber());
        csSmsRecord.setCreateTime(new Date());
        csSmsRecord.setUserId(csDeposit.getUserId());
        //插入记录表
        csSmsRecordMapper.insertCsSmsRecord(csSmsRecord);
        return 1;
    }
}

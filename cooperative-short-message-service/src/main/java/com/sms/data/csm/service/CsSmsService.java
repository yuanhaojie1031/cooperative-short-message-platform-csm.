package com.sms.data.csm.service;

import com.sms.data.csm.model.CsTemplateCode;
import com.sms.data.csm.po.CsSmsPo;

import java.util.List;

public interface CsSmsService {
    /**
     * 根据模板发送短信
     * @param csSmsPo
     */
    int SendCsSms(CsSmsPo csSmsPo);
    void SendBatchSms(List<CsSmsPo> csSmsPos);
    void SendBatchCsSms(CsSmsPo csSmsPos);
    List<CsTemplateCode> selectCsTemplateCodeAll();
    CsTemplateCode selectCsTemplateCode(String templateCode);

}

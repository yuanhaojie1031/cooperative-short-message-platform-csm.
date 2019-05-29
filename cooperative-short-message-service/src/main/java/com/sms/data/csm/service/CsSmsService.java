package com.sms.data.csm.service;

import com.sms.data.csm.model.CsTemplateCode;
import com.sms.data.csm.po.CsSmsPo;

import java.util.List;

public interface CsSmsService {
    /**
     * 根据模板发送短信
     * @param csSmsPo
     */
    Integer SendCsSms(CsSmsPo csSmsPo);
    List<CsTemplateCode> selectCsTemplateCode();

}

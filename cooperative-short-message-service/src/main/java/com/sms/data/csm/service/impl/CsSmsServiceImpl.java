package com.sms.data.csm.service.impl;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.sms.data.csm.common.SMSConfig;
import com.sms.data.csm.enums.SmsTemplate;
import com.sms.data.csm.mapper.CsDepositMapper;
import com.sms.data.csm.mapper.CsSmsMapper;
import com.sms.data.csm.mapper.CsTemplateCodeMapper;
import com.sms.data.csm.model.CsDeposit;
import com.sms.data.csm.model.CsSms;
import com.sms.data.csm.model.CsTemplateCode;
import com.sms.data.csm.po.CsSmsPo;
import com.sms.data.csm.service.CsSmsService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class CsSmsServiceImpl implements CsSmsService {

    private final Logger logger = LoggerFactory.getLogger(CsSmsServiceImpl.class);

    //短信发送mapper
    private CsSmsMapper csSmsMapper;
    //充值余额mapper
    private CsDepositMapper csDepositMapper;
    //短信模板mapper
    private CsTemplateCodeMapper csTemplateCodeMapper;

    @Autowired
    public CsSmsServiceImpl(CsSmsMapper csSmsMapper, CsDepositMapper csDepositMapper, CsTemplateCodeMapper csTemplateCodeMapper) {
        this.csSmsMapper = csSmsMapper;
        this.csDepositMapper = csDepositMapper;
        this.csTemplateCodeMapper = csTemplateCodeMapper;
    }

    @Transactional
    public int SendCsSms(CsSmsPo csSmsPo) {
        int count = 1;
        CsDeposit csDeposit = csDepositMapper.selectCsDeposit(csSmsPo.getUserId());
        if (csDeposit.getSmsNumber() == 0) {
            count =  0;
        } else {
            sendSms(csSmsPo);
        }
    return count;
    }

    public List<CsTemplateCode> selectCsTemplateCodeAll() {
        return csTemplateCodeMapper.selectCsTemplateCodeAll();
    }

    public CsTemplateCode selectCsTemplateCode(String templateCode) {
        return csTemplateCodeMapper.selectCsTemplateCode(templateCode);
    }

    private void sendSms(CsSmsPo csSmsPo) {
        try {
            System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
            System.setProperty("sun.net.client.defaultReadTimeout", "10000");
            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", SMSConfig.accessKeyId, SMSConfig.accessKeySecret);
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", SMSConfig.product, SMSConfig.domain);
            IAcsClient acsClient = new DefaultAcsClient(profile);
            SendSmsRequest request = new SendSmsRequest();
            request.setMethod(MethodType.POST);
            request.setPhoneNumbers(csSmsPo.getCsPhone());
            request.setSignName("乡韵轩");
            request.setTemplateCode(csSmsPo.getTemplateCode());
            String time = "2019-05-27";
            String json = "{\"name\":\"" + csSmsPo.getCsName() + "\",\"time\":\"" + time + "\"}";
            if (StringUtils.isNotBlank(json)) {
                request.setTemplateParam(json);
            }
            SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
            logger.info("短信发送结果:" + JSON.toJSONString(sendSmsResponse));
            if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
                logger.info(csSmsPo.getCsPhone() + "：短信发送成功！");
                insertSms(csSmsPo, request);
            } else {
                logger.error(csSmsPo.getCsPhone() + "：短信发送失败！");
            }
        } catch (ClientException e) {
            logger.error("短信发送异常" + e.getErrCode() + e.getErrMsg());
        }
    }

    private void insertSms(CsSmsPo csSmsPo, SendSmsRequest request) {
        // 发送成功，插入Sms记录表
        CsSms csSms = new CsSms();
        csSms.setPhone(request.getPhoneNumbers());
        csSms.setTemplateCode(request.getTemplateCode());
        csSms.setUserId(csSmsPo.getUserId());
        csSms.setCreateTime(new Date());
        csSms.setSendTime(new Date());
        csSmsMapper.insertCsSms(csSms);
        //发送成功，减去充值表数值
        CsDeposit csDeposit = new CsDeposit();
        csDeposit.setUserId(csSmsPo.getUserId());
        csDeposit.setSmsNumber(-1);
        csDeposit.setUpdateTime(new Date());
        csDepositMapper.updateCsDeposit(csDeposit);
    }


}

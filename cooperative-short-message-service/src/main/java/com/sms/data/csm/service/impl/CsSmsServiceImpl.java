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
        if (csDeposit == null) {
            count = 0;
        } else {
            if (csDeposit.getSmsNumber() == 0) {
                count = 0;
            } else {
                sendSms(csSmsPo);
            }
        }
        return count;
    }

    public void SendBatchCsSms(CsSmsPo csSmsPo) {
        sendSms(csSmsPo);
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
            request.setTemplateCode(csSmsPo.getTemplateCode());
            String json = selectTemplateCode(csSmsPo);
            if (StringUtils.isNotBlank(json)) {
                request.setTemplateParam(json);
            }
            request.setSignName("XYX");
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

    private String selectTemplateCode(CsSmsPo csSmsPo) {
        System.out.println("调用selectTemplateCode方法");
        String json = null;
        if ("SMS_167181265".equals(csSmsPo.getTemplateCode())) {
            json = "{\"name\":\"" + csSmsPo.getCsName() + "\",\"foradd\":\"涉嫌恶意拖欠，多次催收\",\"conx\":\"相关材料\",\"content\":\"司法机关处理\",\"csphone\":\"17743598872\"}";
        } else if ("SMS_167181271".equals(csSmsPo.getTemplateCode())) {
            json = "{\"name\":\"" + csSmsPo.getCsName() + "\",\"foradd\":\"严重逾期情况，以及警告无效，\",\"conx\":\"案处理，保全国家资产，\",\"content\":\"立即还款，以免\",\"sendtext\":\"不良记录\",\"foreach\":\"刑事案件\",\"manage\":\"引起恶性后果\",\"csphone\":\"16602257788\"}";
        } else if ("SMS_167370558".equals(csSmsPo.getTemplateCode())) {
            json = "{\"name\":\"" + csSmsPo.getCsName() + "\",\"foradd\":\"欠款长时间逾期，\",\"text\":\"报失信被执行人名单，\",\"conx\":\"法院向社会公布\",\"sendtext\":\"施强制划扣、限制融资\",\"forback\":\"限制高消费等惩戒。12小时内\",\"handle\":\"上报名单\",\"manage\":\"案件负责人协商还款\",\"phone\":\"16601157503\"}";
        } else if ("SMS_167365639".equals(csSmsPo.getTemplateCode())) {
            json = "{\"name\":\"" + csSmsPo.getCsName() + "\",\"foradd\":\"美团欠款一案多次\",\"text\":\"均不理会，恶意逃避催收\",\"manage\":\"立即采取报案措施，具体\",\"csphone\":\"16601157503\"}";
        } else if ("SMS_167365644".equals(csSmsPo.getTemplateCode())) {
            json = "{\"name\":\"" + csSmsPo.getCsName() + "\",\"foradd\":\"欠款逾期不还，\",\"text\":\"法院提起诉讼/仲裁。12小时内未还款\",\"content\":\"放弃协商，一旦法院受理\",\"forback\":\"传票\",\"manage\":\"诉讼/仲裁准备\",\"csphone\":\"16678448889\"}";
        }
        return json;
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

package com.sms.data.csm.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendBatchSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendBatchSmsResponse;
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

import java.util.ArrayList;
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

    public void SendBatchSms(List<CsSmsPo> csSmsPos) {

        int toIndex=100;
        for(int i = 0;i<csSmsPos.size();i+=100){
            if(i+100>csSmsPos.size()){        //作用为toIndex最后没有100条数据则剩余几条newList中就装几条
                toIndex=csSmsPos.size()-i;
            }
            List<String> phoneNumberJson = new ArrayList<>();
            List<String> signNameJson = new ArrayList<>();
            List<String> templateParamJson = new ArrayList<>();
            List<CsSmsPo> subCsSmsPos = csSmsPos.subList(i,i+toIndex);
            for(int j = 0;j<subCsSmsPos.size();j++){
                phoneNumberJson.add(subCsSmsPos.get(j).getCsPhone());
                signNameJson.add("XYX");
                templateParamJson.add(selectTemplateCode(subCsSmsPos.get(j)));
            }
            try {
                System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
                System.setProperty("sun.net.client.defaultReadTimeout", "10000");
                IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", SMSConfig.accessKeyId, SMSConfig.accessKeySecret);
                DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", SMSConfig.product, SMSConfig.domain);
                IAcsClient acsClient = new DefaultAcsClient(profile);
                SendBatchSmsRequest request = new SendBatchSmsRequest();
                request.setMethod(MethodType.POST);
                request.setPhoneNumberJson(JSONArray.toJSONString(phoneNumberJson));
                request.setTemplateCode(csSmsPos.get(0).getTemplateCode());
                request.setTemplateParamJson(JSONArray.toJSONString(templateParamJson));
                request.setSignNameJson(JSONArray.toJSONString(signNameJson));
                SendBatchSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
                logger.info("短信发送结果:" + JSON.toJSONString(sendSmsResponse));
                if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
                    logger.info("短信发送成功！");
                } else {
                    logger.error("短信发送失败！");
                }
            } catch (ClientException e) {
                logger.error("短信发送异常" + e.getErrCode() + e.getErrMsg());
            }
        }

    }

    private String selectTemplateCode(CsSmsPo csSmsPo) {
        String json = null;
        if ("SMS_167181265".equals(csSmsPo.getTemplateCode())) {
            json = "{\"name\":\"" + csSmsPo.getCsName() + "\",\"foradd\":\"涉 嫌恶意拖欠，多次催 收\",\"conx\":\"相关材料\",\"content\":\"司 法机 关处理\",\"csphone\":\""+csSmsPo.getPublicPhone()+"\"}";
        } else if ("SMS_167181271".equals(csSmsPo.getTemplateCode())) {
            json = "{\"name\":\"【微众银行】" + csSmsPo.getCsName() + "\",\"foradd\":\"长期拖欠微 众银行微粒贷欠款，警告无效，\",\"conx\":\"案处理，保 全国 家资产\",\"content\":\"立即还 款，以免\",\"sendtext\":\"不良记录\",\"foreach\":\"刑 事案件\",\"manage\":\"引起恶性后果\",\"csphone\":\""+csSmsPo.getPublicPhone()+"\"}";
        } else if ("SMS_167370558".equals(csSmsPo.getTemplateCode())) {
            json = "{\"name\":\"" + csSmsPo.getCsName() + "\",\"foradd\":\"欠 款长时间逾期，\",\"text\":\"报失信被执行人名单，\",\"conx\":\"法 院向社会公布\",\"sendtext\":\"施强制划扣、限制融 资\",\"forback\":\"限制高消费等惩戒。12小时内\",\"handle\":\"上报名单\",\"manage\":\"案 件负责人协商还款\",\"phone\":\""+csSmsPo.getPublicPhone()+"\"}";
        } else if ("SMS_167365639".equals(csSmsPo.getTemplateCode())) {
            json = "{\"name\":\"" + csSmsPo.getCsName() + "\",\"foradd\":\"美 团欠 款一案多次\",\"text\":\"均不理会，恶意逃避催 收\",\"manage\":\"立即采取报 案措施，具体\",\"csphone\":\""+csSmsPo.getPublicPhone()+"\"}";
        } else if ("SMS_167365644".equals(csSmsPo.getTemplateCode())) {
            json = "{\"name\":\"" + csSmsPo.getCsName() + "\",\"foradd\":\"欠 款逾期不还，\",\"text\":\"法 院提起诉 讼/仲 裁。12小时内未还款\",\"content\":\"放弃协商，一旦法 院受理\",\"forback\":\"传票\",\"manage\":\"诉 讼/仲 裁准备\",\"csphone\":\""+csSmsPo.getPublicPhone()+"\"}";
        }else if ("SMS_167396652".equals(csSmsPo.getTemplateCode())) {
            json = "{\"name\":\"" + csSmsPo.getCsName() + "\",\"address\":\"地址\",\"sendtext\":\"案 件资料和相关文件\",\"manage\":\"你领导调解，有疑问\",\"csphone\":\""+csSmsPo.getPublicPhone()+"\"}";
        }else if ("SMS_167401552".equals(csSmsPo.getTemplateCode())) {
            json = "{\"sendtext\":\"招 联金 融欠 款案 件委托\",\"forback\":\"24小时候进行全 国公布\",\"handle\":\"将上传（全 国失 信人员老 赖名单）\",\"lastsend\":\"12小时内\",\"csphone\":\""+csSmsPo.getPublicPhone()+"\"}";
        }else if ("SMS_167396467".equals(csSmsPo.getTemplateCode())) {
            json = "{\"name\":\"" + csSmsPo.getCsName() + "\",\"document\":\"欠 款拖欠一案，报 案文书\",\"address\":\"当地分 局。24小时内\",\"handle\":\"立 案结果，限你于12小时内回电\",\"phone\":\""+csSmsPo.getPublicPhone()+"\"}";
        }else if ("SMS_167960671".equals(csSmsPo.getTemplateCode())) {
            json = "{\"name\":\"" + csSmsPo.getCsName() + "\",\"sendtext\":\"公安执法人员及我方律师团队已上门取证\",\"forback\":\"立案审核，报案文书\",\"address\":\"户籍所在地，24小时内\",\"manage\":\"立案结果，限你于24小时内回电说明\",\"phone\":\""+csSmsPo.getPublicPhone()+"\"}";
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

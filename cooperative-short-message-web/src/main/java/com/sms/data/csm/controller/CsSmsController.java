package com.sms.data.csm.controller;

import com.sms.data.csm.BaseController;
import com.sms.data.csm.common.BaseResult;
import com.sms.data.csm.common.ResultCode;
import com.sms.data.csm.model.CsTemplateCode;
import com.sms.data.csm.model.CsUser;
import com.sms.data.csm.po.CsSmsPo;
import com.sms.data.csm.service.CsSmsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 短信入口
 * 2019-05-21
 */
@Api(tags="短信接口")
@Controller
@RequestMapping("/csSms")
public class CsSmsController extends BaseController {

    private CsSmsService smsService;

    @Autowired
    public CsSmsController(CsSmsService smsService){
        this.smsService = smsService;
    }

    @ApiOperation(value = "测试消息", notes = "测试消息")
    @RequestMapping(value = "/sms", method = RequestMethod.GET)
    @ResponseBody
    public void sms() {
        CsSmsPo csSmsPo = new CsSmsPo();
        smsService.SendCsSms(csSmsPo);
    }

    @ApiOperation(value = "消息发送页面", notes = "消息发送页面")
    @RequestMapping(value = "/sms", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult sms(@RequestBody CsSmsPo csSmsPo) {
        //获取登录用户信息
        CsUser csUser = getLoginUser();
        csSmsPo.setUserId(csUser.getId());
        return new BaseResult(ResultCode.Success, 0,smsService.SendCsSms(csSmsPo));
    }

    @ApiOperation(value = "查询消息模板", notes = "查询消息模板")
    @RequestMapping(value = "/selectCsTemplateCode", method = RequestMethod.GET)
    @ResponseBody
    public BaseResult selectCsTemplateCode() {
        return new BaseResult(ResultCode.Success, 0,smsService.selectCsTemplateCode());
    }

}

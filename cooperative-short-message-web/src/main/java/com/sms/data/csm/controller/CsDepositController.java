package com.sms.data.csm.controller;

import com.sms.data.csm.BaseController;
import com.sms.data.csm.common.BaseResult;
import com.sms.data.csm.common.ResultCode;
import com.sms.data.csm.model.CsDeposit;
import com.sms.data.csm.model.CsUser;
import com.sms.data.csm.service.CsDepositService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 充值入口
 * 2019-05-21
 */
@Api(tags="充值接口")
@Controller
@RequestMapping("/csDeposit")
public class CsDepositController extends BaseController {

    private CsDepositService csDepositService;

    @Autowired
    public CsDepositController(CsDepositService csDepositService){
        this.csDepositService = csDepositService;
    }

    @ApiOperation(value = "充值页面", notes = "充值页面")
    @RequestMapping(value = "/deposit", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult deposit(@RequestBody CsDeposit csDeposit) {
        csDepositService.insertCsDeposit(csDeposit);
        return new BaseResult(ResultCode.Success, 0);
    }

}

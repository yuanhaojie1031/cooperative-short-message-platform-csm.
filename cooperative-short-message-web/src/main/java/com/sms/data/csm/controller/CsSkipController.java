package com.sms.data.csm.controller;

import com.sms.data.csm.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 登录入口
 * 2019-05-21
 */
@Api(tags="登录接口")
@Controller
@RequestMapping("/csSkip")
public class CsSkipController extends BaseController {

    @ApiOperation(value = "登录跳转", notes = "登录跳转")
    @RequestMapping(value = "/toLogin", method = RequestMethod.GET)
    public String toLogin() {
        System.out.println("进入toLogin方法");
        return "login";
    }

    @ApiOperation(value = "注册跳转", notes = "注册跳转")
    @RequestMapping(value = "/toRegist", method = RequestMethod.GET)
    public String toRegist() {
        System.out.println("进入toRegist方法");
        return "regist";
    }

    @ApiOperation(value = "发送跳转", notes = "发送跳转")
    @RequestMapping(value = "/toSms", method = RequestMethod.GET)
    public String toSms() {
        System.out.println("进入toSms方法");
        return "sms";
    }

    @ApiOperation(value = "发送跳转", notes = "发送跳转")
    @RequestMapping(value = "/toExcelPoi", method = RequestMethod.GET)
    public String toExcelPoi() {
        System.out.println("进入toExcelPoi方法");
        return "excelPoi";
    }

    @ApiOperation(value = "充值跳转", notes = "充值跳转")
    @RequestMapping(value = "/toDeposit", method = RequestMethod.GET)
    public String toDeposit() {
        System.out.println("进入toDeposit方法");
        return "deposit";
    }
}

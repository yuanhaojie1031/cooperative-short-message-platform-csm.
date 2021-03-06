package com.sms.data.csm.controller;

import com.sms.data.csm.BaseController;
import com.sms.data.csm.common.BaseResult;
import com.sms.data.csm.common.ResultCode;
import com.sms.data.csm.model.CsUser;
import com.sms.data.csm.service.CsUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 用户入口
 * 2019-05-21
 */
@Api(tags="用户接口")
@Controller
@RequestMapping("/csUser")
public class CsUserController extends BaseController {

    private CsUserService csUserService;

    @Autowired
    public CsUserController(CsUserService csUserService){
        this.csUserService = csUserService;
    }

    @ApiOperation(value = "用户登录", notes = "用户登录")
    @RequestMapping(value = "/login", method = RequestMethod.POST )
    @ResponseBody
    public BaseResult login(@RequestBody CsUser csUser, HttpServletRequest request) {
        CsUser csUserDTO = csUserService.login(csUser);
        HttpSession session=request.getSession();
        session.setAttribute("userId",csUserDTO.getId());
        return new BaseResult(ResultCode.Success, 0,csUserDTO);
    }

    @ApiOperation(value = "新增用户", notes = "新增用户")
    @RequestMapping(value = "/insertCsUser", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult insertCsUser(@RequestBody CsUser csUser) {
        return new BaseResult(ResultCode.Success, 0,csUserService.insertCsUser(csUser));
    }

    @ApiOperation(value = "查询用户列表", notes = "查询用户列表")
    @RequestMapping(value = "/selectCsUser", method = RequestMethod.GET)
    @ResponseBody
    public BaseResult selectCsUser() {
        return new BaseResult(ResultCode.Success, 0,csUserService.selectCsUser());
    }
}

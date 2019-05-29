package com.sms.data.csm;


import com.sms.data.csm.model.CsUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

/**
 * 所有controller的基类
 *
 * @author yuanhaojie
 * @create 2019-05-27 15:28
 **/
public class BaseController {


    /**
     * 返回当前登录登录的用户
     *
     * @return
     */
    public CsUser getLoginUser() {
        Subject subject = SecurityUtils.getSubject();
        CsUser csUser = null;
        if (null != subject) {
            csUser = (CsUser) subject.getPrincipal();// subject.getSession().getAttribute("shiroLoginUser");
            if (csUser == null) {
                throw new ArithmeticException("登录超时！");
            }
        } else {
            throw new ArithmeticException("登录超时！");
        }
        return csUser;
    }

    /**
     * 获取当前用户session
     *
     * @return
     */
    public Session getSession() {
        Subject subject = SecurityUtils.getSubject();
        if (null != subject) {
            return subject.getSession();
        } else {
            return null;
        }

    }
}

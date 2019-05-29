package com.sms.data.csm.model;

import java.util.Date;

public class CsUser {
    private  int id;
    private String csName;
    private String csPwd;
    private String phone;
    private Date createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCsName() {
        return csName;
    }

    public void setCsName(String csName) {
        this.csName = csName;
    }

    public String getCsPwd() {
        return csPwd;
    }

    public void setCsPwd(String csPwd) {
        this.csPwd = csPwd;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}

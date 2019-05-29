package com.sms.data.csm.model;

import java.util.Date;

public class CsSmsRecord {
    private int id;
    private Date createTime;
    private int smsNumber;
    private int userId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getSmsNumber() {
        return smsNumber;
    }

    public void setSmsNumber(int smsNumber) {
        this.smsNumber = smsNumber;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}

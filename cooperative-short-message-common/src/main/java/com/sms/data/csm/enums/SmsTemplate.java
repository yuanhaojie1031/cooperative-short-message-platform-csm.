package com.sms.data.csm.enums;

public enum SmsTemplate {
    /**
     * 测试模板
     */
    TEST("SMS_166371825","测试模板",0),

    // 验证码模板
    Identifying_Code("SMS_105560056","验证码模板",0),
    Identifying_Code_WX("WX_105560056","验证码模板",0);

    SmsTemplate(String code,String name,int hour) {
        this.code = code;
        this.name = name;
        this.hour = hour;
    }

    SmsTemplate(String code,String name,int hour,String url,String first,String remark) {
        this.code = code;
        this.name = name;
        this.hour = hour;
        this.url = url;
        this.first = first;
        this.remark = remark;
    }

    SmsTemplate(String code, String templateCode, String name,int hour,String url,String first,String remark) {
        this.code = code;
        this.templateCode = templateCode;
        this.name = name;
        this.hour = hour;
        this.url = url;
        this.first = first;
        this.remark = remark;
    }

    public  static SmsTemplate getSmsTemplateByCode(String code) {
        SmsTemplate[] templates = SmsTemplate.values();
        for(SmsTemplate templ: templates) {
            if(templ.getCode().equals(code)) {
                return templ;
            }
        }
        return null;
    }

    private String code;

    private String templateCode;

    private String name;

    private int hour;

    private String url;

    private String first;

    private String remark;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

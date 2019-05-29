package com.sms.data.csm.common;

import java.io.Serializable;

/**
 * 通用返回结果
 *
 * @author yuanhaojie
 * @create 2017-07-03 14:25
 **/
public class BaseResult<T> implements Serializable {
	private static final long serialVersionUID = 6418812617728335699L;
	
	private Boolean success;
    private Integer code;
    private T data;

    public BaseResult(Boolean success, Integer code) {
        this.success = success;
        this.code = code;
    }

    public BaseResult(Boolean success, Integer code, T data) {
        this.success = success;
        this.code = code;
        this.data = data;
    }

    public BaseResult(Boolean success, T data) {
        this.success = success;
        this.code = 0;
        this.data = data;
    }

    public BaseResult(Boolean success) {
        this.success = success;
        this.code = 0;
    }

    public BaseResult() {
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

	public static BaseResult<Object> ok() {
		return new BaseResult<Object>(true);
	}
	
	public static BaseResult<Object> ok(Object data) {
		return  new BaseResult<Object>(true,data);
	}
}

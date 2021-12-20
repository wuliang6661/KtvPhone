package com.ipad.ktvphone.entity;

/**
 * Created by wuliang on 2017/3/27.
 * 所有返回的json数据的公有格式
 */

public class BaseResult<T> {

//    1）	status: 表成功和失败状态。1表成功，0表失败。
//            2）	errorMessage: 错误信息，当有错误发生时，此errorMessage包含有错误信息
//    3）	errorCode: 错误编码，当有错误发生时，此errorCode包含有错误编码
//    4）	data：返回数据

    private static String SURCESS = "1";

    private String status;

    private String errorMessage;

    private String errorCode;

    private T data;

    public boolean surcess() {
        return SURCESS.equals(status);
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}

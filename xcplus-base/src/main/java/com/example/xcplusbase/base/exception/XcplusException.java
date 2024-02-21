package com.example.xcplusbase.base.exception;
/*
 * author: oliver
 * date: 2024-02-16 19:32
 */

public class XcplusException extends RuntimeException {
    private String errMessage;

    private String errCode;

    public XcplusException() {
    }

    public XcplusException(String errMessage) {
        this.errMessage = errMessage;
    }


    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public String getErrCode() {
        return errCode;
    }

    public XcplusException(String message, String errMessage) {
        super(message);
        this.errCode= message;
        this.errMessage = errMessage;
    }
    public  static void cast(String message){
        throw new RuntimeException(message);

    }

    public  static void cast(CommonErrorEnum commonErrorEnum){
        throw new RuntimeException(commonErrorEnum.getErrMessage());

    }
}
